package com.android.instagram.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Ajay Deepak on 26-07-2019.
 */
data class PostCreateResponse(
    @Expose
    @SerializedName("statusCode")
    var statusCode: String,

    @Expose
    @SerializedName("status")
    val status: Int,

    @Expose
    @SerializedName("message")
    var message: String,

    @Expose
    @SerializedName("data")
    val data: PostData
) {
    data class PostData(

        @Expose
        @SerializedName("id")
        val id: String,

        @Expose
        @SerializedName("imgUrl")
        val imgUrl: String,

        @Expose
        @SerializedName("imgWidth")
        val imgWidth: Int?,

        @Expose
        @SerializedName("imgHeight")
        val imgHeight: Int?,

        @Expose
        @SerializedName("createdAt")
        val createdAt: Date

    )
}