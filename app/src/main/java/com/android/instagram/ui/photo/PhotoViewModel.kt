package com.android.instagram.ui.photo

import androidx.lifecycle.MutableLiveData
import com.android.instagram.R
import com.android.instagram.data.model.Image
import com.android.instagram.data.model.Post
import com.android.instagram.data.remote.response.PostCreateResponse
import com.android.instagram.data.repository.PhotoRepository
import com.android.instagram.data.repository.PostListRepository
import com.android.instagram.data.repository.UserRepository
import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.common.Event
import com.android.instagram.utils.common.FileUtils
import com.android.instagram.utils.common.Resource
import com.android.instagram.utils.log.Logger
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream

/**
 * Created by Ajay Deepak on 10-07-2019.
 */
class PhotoViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val postListRepository: PostListRepository,
    private val directory: File,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Post>> = MutableLiveData()

    val user = userRepository.getCurrentUser()!!

    override fun onCreate() {}

    fun onGalleryImageSelected(inputStream: InputStream) {

        Logger.d("DEBUG", "onGalleryImageSelected")

        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable {
                FileUtils.saveInputStreamToFile(inputStream, directory, "gallery_img_temp", 500)
            }.subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                    if (it != null) {
                        FileUtils.getImageSize(it)?.run {
                            uploadAndCreatePost(it, this)
                        }
                    } else {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }

                }, {
                    loading.postValue(false)
                    messageStringId.postValue(Resource.error(R.string.try_again))
                })
        )
    }

    fun onCameraImageTaken(camerImageProcessor: () -> String) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable { camerImageProcessor() }
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    File(it).apply {
                        FileUtils.getImageSize(this)?.let { size ->
                            uploadAndCreatePost(this, size)
                        }?: loading.postValue(false)
                    }
                },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    })
        )
    }

    private fun uploadAndCreatePost(file: File, imageSize: Pair<Int, Int>) {

        Logger.d("DEBUG", file.path)
        compositeDisposable.add(
            photoRepository.uploadImage(file, user)
                .flatMap {
                    Logger.d("DEBUG", it)
                    postListRepository.doPostCreate(it, imageSize.first, imageSize.second, user)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    loading.postValue(false)
                    post.postValue(Event(it))
                }
                    , {
                        handleNetworkError(it)
                        loading.postValue(false)
                    })
        )


    }
}