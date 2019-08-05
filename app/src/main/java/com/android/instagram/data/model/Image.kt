package com.android.instagram.data.model

import retrofit2.http.Url

/**
 * Created by Ajay Deepak on 16-07-2019.
 */
data class Image(
    val url: String,
    val headers:Map<String, String>,
    val placeholderWidth: Int = -1,
    val placeholderHeight: Int = -1
) {

}