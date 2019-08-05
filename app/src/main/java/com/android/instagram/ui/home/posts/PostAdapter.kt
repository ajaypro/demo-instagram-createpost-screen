package com.android.instagram.ui.home.posts

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.android.instagram.data.model.Post
import com.android.instagram.ui.base.BaseAdapter

/**
 * Created by Ajay Deepak on 16-07-2019.
 */
class PostAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>)
    : BaseAdapter<Post, PostItemViewHolder>(parentLifecycle, posts) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder = PostItemViewHolder(parent)


}