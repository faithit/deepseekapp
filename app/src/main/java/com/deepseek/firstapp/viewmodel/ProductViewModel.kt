package com.deepseek.firstapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class ProductViewModel (navController: NavHostController,var context: Context){
    var cloudinaryUrl="https://api.cloudinary.com/v1_1/dojp0mlml/upload" //do...use own cloud name
    var uploadPreset="newproducts"
    val databasareference= FirebaseDatabase.getInstance().getReference("Products")
    //functions
    //crud c-create,r-read,u-update,d-delete
    //upload product  to firebase function
    fun uploadProduct(){

    }
    //upload image to clodinary function using okthttp
    //extracts the secure image url from the response and returns the url
    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes()
            ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()
        val request = Request.Builder()
            .url(cloudinaryUrl)
            .post(requestBody)
            .build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }


    //fetch product function
    //update p[roduct function
    //delete product function

}