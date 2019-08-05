package com.android.instagram.di.component

import com.android.instagram.di.ActivityScope
import com.android.instagram.di.module.ActivityModule
import com.android.instagram.ui.dummy.DummyActivity
import com.android.instagram.ui.login.LoginActivity
import com.android.instagram.ui.main.MainActivity
import com.android.instagram.ui.splash.SplashActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: DummyActivity)

    fun inject(loginActivity: LoginActivity)

    fun inject(mainActivity: MainActivity)
}