package com.android.instagram.data.remote.response

import com.android.instagram.data.model.Post
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ajay Deepak on 13-07-2019.
 */

data class PostListResponse(

    @Expose
    @SerializedName("statusCode")
    var statusCode: String,

    @Expose
    @SerializedName("message")
    var message: String,

    @Expose
    @SerializedName("data")
    val data: List<Post>
    )