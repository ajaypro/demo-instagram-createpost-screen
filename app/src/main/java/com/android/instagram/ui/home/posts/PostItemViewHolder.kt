package com.android.instagram.ui.home.posts

import android.view.View
import android.view.ViewGroup
import com.android.instagram.R
import com.android.instagram.data.model.Post
import com.android.instagram.di.component.ViewHolderComponent
import com.android.instagram.ui.base.BaseItemViewHolder
import com.android.instagram.ui.base.BaseViewModel

/**
 * Created by Ajay Deepak on 16-07-2019.
 */
class PostItemViewHolder(parent: ViewGroup): BaseItemViewHolder<Post, PostItemViewModel>(R.layout.item_view_post, parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
    }

    override fun setupView(view: View) {
    }
}