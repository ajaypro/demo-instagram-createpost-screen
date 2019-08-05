package com.android.instagram.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ajay Deepak on 29-07-2019.
 */
data class ImageResponse (

    @Expose
    @SerializedName("statusCode")
    val statusCode: String,

    @Expose
    @SerializedName("status")
    val status: Int,

    @Expose
    @SerializedName("message")
    val message: String,

    @Expose
    @SerializedName("data")
    val data: ImageData
) {

    data class ImageData(
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String
    )
}