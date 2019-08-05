package com.android.instagram.di.component

import com.android.instagram.di.FragmentScope
import com.android.instagram.di.module.FragmentModule
import com.android.instagram.ui.dummies.DummiesFragment
import com.android.instagram.ui.home.HomeFragment
import com.android.instagram.ui.photo.PhotoFragment
import com.android.instagram.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)

    fun inject(photoFragment: PhotoFragment)

    fun inject(homeFragment: HomeFragment)

    fun inject(profileFragment: ProfileFragment)
}