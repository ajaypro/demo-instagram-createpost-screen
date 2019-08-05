package com.android.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.instagram.data.repository.DummyRepository
import com.android.instagram.data.repository.PhotoRepository
import com.android.instagram.data.repository.PostListRepository
import com.android.instagram.data.repository.UserRepository
import com.android.instagram.di.TempDirectory
import com.android.instagram.ui.base.BaseFragment
import com.android.instagram.ui.dummies.DummiesAdapter
import com.android.instagram.ui.dummies.DummiesViewModel
import com.android.instagram.ui.home.HomeViewModel
import com.android.instagram.ui.home.posts.PostAdapter
import com.android.instagram.ui.main.MainSharedViewModel
import com.android.instagram.ui.photo.PhotoViewModel
import com.android.instagram.ui.photo.ProfileViewModel
import com.android.instagram.utils.ViewModelProviderFactory
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import com.mindorks.paracamera.Camera
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import java.io.File

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideDummiesViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        dummyRepository: DummyRepository
    ): DummiesViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(DummiesViewModel::class) {
                DummiesViewModel(schedulerProvider, compositeDisposable, networkHelper, dummyRepository)
            }
        ).get(DummiesViewModel::class.java)

    @Provides
    fun provideDummiesAdapter() = DummiesAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun providePostsAdapter() = PostAdapter(fragment.lifecycle, ArrayList())


    @Provides
    fun providePhotoViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        userRepository: UserRepository,
        postListRepository: PostListRepository,
        photoRepository: PhotoRepository,
        networkHelper: NetworkHelper,
        @TempDirectory directory: File
    ): PhotoViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(PhotoViewModel::class) {
                PhotoViewModel(schedulerProvider, compositeDisposable, userRepository, photoRepository, postListRepository,
                    directory, networkHelper)
            }
        ).get(PhotoViewModel::class.java)

    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): ProfileViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(ProfileViewModel::class) {
                ProfileViewModel(schedulerProvider, compositeDisposable, networkHelper)
            }
        ).get(ProfileViewModel::class.java)

    @Provides
    fun provideHomeViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        postListRepository: PostListRepository
    ): HomeViewModel =
        ViewModelProviders.of(fragment,
            ViewModelProviderFactory(HomeViewModel::class) {
                HomeViewModel(
                    schedulerProvider, compositeDisposable, networkHelper, userRepository,
                    postListRepository,ArrayList(), PublishProcessor.create()
                )
            }
        ).get(HomeViewModel::class.java)


     @Provides
    fun provideCamera() = Camera.Builder()
        .resetToCorrectOrientation(true)
        .setTakePhotoRequestCode(1)
        .setDirectory("temp")
        .setName("cam_image")
         .setImageFormat(Camera.IMAGE_JPG)
        .setCompression(75)
        .setImageHeight(500)
        .build(fragment)

    @Provides
    fun provideMainSharedViewModel(schedulerProvider: SchedulerProvider,
                                   compositeDisposable: CompositeDisposable,
                                   networkHelper: NetworkHelper
    ): MainSharedViewModel
            = ViewModelProviders.of(fragment.activity!!,ViewModelProviderFactory(MainSharedViewModel::class) {
        MainSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
    }).get(MainSharedViewModel::class.java)
}