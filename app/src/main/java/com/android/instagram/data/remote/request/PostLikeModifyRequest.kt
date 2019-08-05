package com.android.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ajay Deepak on 13-07-2019.
 */

data class PostLikeModifyRequest(

    @Expose
    @SerializedName("postId")
    var postId: String
)