package com.android.instagram.ui.home.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.instagram.R
import com.android.instagram.data.model.Image
import com.android.instagram.data.model.Post
import com.android.instagram.data.remote.Networking
import com.android.instagram.data.repository.PostListRepository
import com.android.instagram.data.repository.UserRepository
import com.android.instagram.ui.base.BaseItemViewModel
import com.android.instagram.utils.common.Resource
import com.android.instagram.utils.common.TimeUtils
import com.android.instagram.utils.display.ScreenUtils
import com.android.instagram.utils.log.Logger
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Ajay Deepak on 16-07-2019.
 */

class PostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postListRepository: PostListRepository
) : BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "PostItemViewModel"
    }

    private val user = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken),
        Pair(Networking.HEADER_USER_ID, user.id)
    )

    val name: LiveData<String> = Transformations.map(data) { it.creator.name }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.size ?: 0 }

    val isLiked: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy?.find { postUser -> postUser.id == user.id } != null
    }
    val postTime: LiveData<String> = Transformations.map(data) { TimeUtils.getTimeAgo(it.createdAt) }

    val profileImage: LiveData<Image> = Transformations.map(data)
    { it.creator.profilePicUrl?.run { Image(this, headers) } }

    val imageDetail: LiveData<Image> = Transformations.map(data){
       Image(
           it.imageUrl,
           headers,
           screenWidth,
           it.imageHeight?.let{height ->
               return@let (calculateScaleFactor(it) * height).toInt()
           } ?: screenHeight/3)
    }

    private fun calculateScaleFactor(post: Post) =
        post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f



    override fun onCreate() {
        Logger.d(TAG, "onCreate called")
    }


    fun onLikeClick() = data.value?.let {

        if (networkHelper.isNetworkConnected()) {

            val api = if (isLiked.value == true) {
                postListRepository.makePostUnlikeCall(it, user)
            } else {
                postListRepository.makePostLikeCall(it, user)
            }
            compositeDisposable.add(api
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { responsePost -> if (responsePost.id == it.id) updateData(responsePost) },
                    { error -> handleNetworkError(error) }
                ))
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }

    }

}