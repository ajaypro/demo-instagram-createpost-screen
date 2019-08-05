package com.android.instagram.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.android.instagram.BuildConfig
import com.android.instagram.InstagramApplication
import com.android.instagram.data.local.db.DatabaseService
import com.android.instagram.data.local.prefs.UserPreferences
import com.android.instagram.data.remote.NetworkService
import com.android.instagram.data.remote.Networking
import com.android.instagram.di.ApplicationContext
import com.android.instagram.di.TempDirectory
import com.android.instagram.utils.common.FileUtils
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.RxSchedulerProvider
import com.android.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: InstagramApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    /**
     * Since this function do not have @Singleton then each time CompositeDisposable is injected
     * then a new instance of CompositeDisposable will be provided
     */
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("bootcamp-instagram-project-prefs", Context.MODE_PRIVATE)

    /**
     * We need to write @Singleton on the provide method if we are create the instance inside this method
     * to make it singleton. Even if we have written @Singleton on the instance's class
     */
    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService =
        Room.databaseBuilder(
            application, DatabaseService::class.java,
            "bootcamp-instagram-project-db"
        ).build()

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.API_KEY,
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper = NetworkHelper(application)

    /**
     * adding this qualifier as file is generic so in future when we want to create file we will have separate
     * qualifier to easily identify
     */
    @Singleton
    @Provides
    @TempDirectory
    fun provideTempDirectory(): File = FileUtils.getDirectory(application, "temp")


}