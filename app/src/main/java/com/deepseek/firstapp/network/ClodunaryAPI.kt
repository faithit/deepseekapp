package com.deepseek.firstapp.network

import com.deepseek.firstapp.models.CloudinaryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ClodunaryAPI{
    @Multipart
    @POST("v1_1/dojp0mlml/image/upload") //replace dojp0mlml with your cloud name
    suspend fun uploadImage(
       @Part  file: MultipartBody.Part,
       @Part("upload_preset") uploadPreset: RequestBody
    ): Response<CloudinaryResponse>
}