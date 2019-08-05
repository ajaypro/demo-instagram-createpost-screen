package com.android.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ajay Deepak on 26-07-2019.
 */

data class PostCreateRequest(

    @Expose
    @SerializedName("imgUrl")
    var imgUrl: String,

    @Expose
    @SerializedName("imgWidth")
    var imgWidth: Int,

    @Expose
    @SerializedName("imgHeight")
    var imgHeight: Int

)