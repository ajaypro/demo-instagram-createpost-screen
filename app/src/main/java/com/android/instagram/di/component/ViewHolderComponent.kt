package com.android.instagram.di.component

import com.android.instagram.di.ViewModelScope
import com.android.instagram.di.module.ViewHolderModule
import com.android.instagram.ui.dummies.DummyItemViewHolder
import com.android.instagram.ui.home.posts.PostItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewHolder: DummyItemViewHolder)
    fun inject(viewHolder: PostItemViewHolder)
}