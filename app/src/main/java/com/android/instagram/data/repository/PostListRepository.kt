package com.android.instagram.data.repository

import com.android.instagram.data.local.db.DatabaseService
import com.android.instagram.data.local.prefs.UserPreferences
import com.android.instagram.data.model.Post
import com.android.instagram.data.model.User
import com.android.instagram.data.remote.NetworkService
import com.android.instagram.data.remote.request.PostCreateRequest
import com.android.instagram.data.remote.request.PostLikeModifyRequest
import com.android.instagram.utils.log.Logger
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Ajay Deepak on 13-07-2019.
 */

class PostListRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {


    fun doPostListCall(firstPostId: String?, lastPostId: String?, user: User): Single<List<Post>> {
        return networkService.doHomePostListCall(
            firstPostId,
            lastPostId,
            user.accessToken,
            user.id
        ).map { it.data }
    }

    // networking call of PUT for user liking the post
    fun makePostLikeCall(post: Post, user: User): Single<Post> {
        return networkService.doPostLikeCall(
            PostLikeModifyRequest(post.id),
            user.accessToken,
            user.id
        ).map {
            post.likedBy?.apply {
                find { postUser -> postUser.id == user.id } ?: add(
                    Post.User(
                    user.id,
                    user.name,
                    user.profilePicUrl)
                )
            }
            return@map post
        }
    }
    // remove user from the liked list
    fun makePostUnlikeCall(post: Post, user: User):Single<Post>{
        return networkService.doPostUnlikeCall(
            PostLikeModifyRequest(post.id),
            user.accessToken,
            user.id).map {
            post.likedBy?.apply {
              find { postUser -> postUser.id == user.id }?.let {
                  remove(it)
              }
            }
            return@map post
            }
        }

    fun doPostCreate(imageUrl: String, imgWidth: Int, imgHeight: Int, user: User): Single<Post> =
        networkService.doPostCreateCall(
            PostCreateRequest(imageUrl, imgWidth, imgHeight),
            user.id,
            user.accessToken
        )
            .map {

                Logger.d("DEBUG", "doPostCreate")
                Post(
                    it.data.id,
                    it.data.imgUrl,
                    it.data.imgWidth,
                    it.data.imgHeight,
                    Post.User(
                        user.id,
                        user.name,
                        user.profilePicUrl
                    ),
                    mutableListOf(),
                    it.data.createdAt
                )
            }
    }
