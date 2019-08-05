package com.android.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.android.instagram.data.model.Post
import com.android.instagram.data.model.User
import com.android.instagram.data.repository.PostListRepository
import com.android.instagram.data.repository.UserRepository
import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.common.Resource
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

/**
 * Created by Ajay Deepak on 10-07-2019.
 */

class HomeViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val postListRepository: PostListRepository,
    private val allPostList: ArrayList<Post>,
    private val paginator: PublishProcessor<Pair<String?, String?>>
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val refreshPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    var firstId: String? = null
    var  lastId: String? = null

    private val user: User = userRepository.getCurrentUser()!! // should not be used without logged in user

    init {
        compositeDisposable.add(
            paginator
                .onBackpressureDrop()
                .doOnNext {
                    loading.postValue(true)
                }
                .concatMapSingle { pageIds ->
                    return@concatMapSingle postListRepository
                        .doPostListCall(pageIds.first, pageIds.second, user)
                        .subscribeOn(Schedulers.io())
                        .doOnError {
                            handleNetworkError(it)
                        }
                }
                .subscribe(
                    {
                        allPostList.addAll(it)

                        firstId = allPostList.maxBy { post -> post.createdAt.time }?.id
                        lastId = allPostList.minBy { post -> post.createdAt.time }?.id

                        loading.postValue(false)
                        posts.postValue(Resource.success(it))
                    },
                    {
                        loading.postValue(false)
                        handleNetworkError(it)
                    }
                )
        )
    }

    override fun onCreate() {
        loadMorePosts()
    }

    private fun loadMorePosts() {
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstId, lastId))
    }

    fun onLoadMore() {
        if (loading.value !== null && loading.value == false) loadMorePosts()
    }

    fun onNewPost(post: Post) {
        allPostList.add(0, post)
        refreshPosts.postValue(
            Resource.success(
                mutableListOf<Post>()
                    .apply { addAll(allPostList) })
        )


    }
}