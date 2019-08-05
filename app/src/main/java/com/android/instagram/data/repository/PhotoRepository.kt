package com.android.instagram.data.repository

import com.android.instagram.data.model.User
import com.android.instagram.data.remote.NetworkService
import com.android.instagram.utils.log.Logger
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Ajay Deepak on 29-07-2019.
 */

class PhotoRepository @Inject constructor(private val networkService: NetworkService) {

    fun uploadImage(file: File, user: User): Single<String> {
        return MultipartBody.Part.createFormData(
            "image", file.path, RequestBody.create(MediaType.parse("image/*"), file)
        ).run {
            Logger.d("DEBUG", "uploadImage")
            return@run networkService.doImageUpload(this, user.id, user.accessToken)
                .map {
                    Logger.d("DEBUG", it.data.imageUrl)
                    it.data.imageUrl }
        }

    }
}