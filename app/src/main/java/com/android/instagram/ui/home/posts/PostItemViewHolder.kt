package com.android.instagram.ui.home.posts

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.android.instagram.R
import com.android.instagram.data.model.Post
import com.android.instagram.di.component.ViewHolderComponent
import com.android.instagram.ui.base.BaseItemViewHolder
import com.android.instagram.utils.common.GlideHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_view_post.view.*

/**
 * Created by Ajay Deepak on 16-07-2019.
 *
 * This is the class that will show the data in the ui for single post
 */
class PostItemViewHolder(parent: ViewGroup) :
    BaseItemViewHolder<Post, PostItemViewModel>(R.layout.item_view_post, parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, Observer {
            itemView.puName.text = it
        })

        viewModel.postTime.observe(this, Observer {
            itemView.puPostTime.text = it
        })

        viewModel.likesCount.observe(this, Observer {
            itemView.puLikesCount.text = itemView.context.getString(R.string.post_likes, it)
        })

        viewModel.isLiked.observe(this, Observer {
            if (it) itemView.puLikePic.setImageResource(R.drawable.ic_heart_selected)
            else itemView.puLikePic.setImageResource(R.drawable.ic_heart_unselected)
        })

        viewModel.profileImage.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.puProfilePic.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = itemView.puProfilePic.layoutParams as ViewGroup.LayoutParams
                    params.height = placeholderHeight
                    params.width = placeholderWidth
                    itemView.puProfilePic.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }

                glideRequest.into(itemView.puProfilePic)
            }

        })

        viewModel.imageDetail.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(itemView.puPost.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                if (placeholderHeight > 0 && placeholderWidth > 0) {
                    val params = itemView.puPost.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    itemView.puPost.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_photo))
                }

                glideRequest.into(itemView.puPost)
            }
        })
    }

    override fun setupView(view: View) {
           itemView.puLikePic.setOnClickListener { viewModel.onLikeClick() }
    }
}