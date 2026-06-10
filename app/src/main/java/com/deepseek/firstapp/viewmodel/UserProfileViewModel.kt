package com.deepseek.firstapp.viewmodel



import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class UserProfileViewModel(
    private val context: Context
) {

    private val auth = FirebaseAuth.getInstance()

    private val cloudinaryUrl =
        "https://api.cloudinary.com/v1_1/dojp0mlml/upload"

    private val uploadPreset = "newproducts"

    fun uploadProfilePicture(uri: Uri?) {

        if (uri == null) return

        val userId = auth.currentUser?.uid ?: return

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val imageUrl =
                    uploadToCloudinary(context, uri)

                FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userId)
                    .child("profileImage")
                    .setValue(imageUrl)

            } catch (e: Exception) {

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun uploadToCloudinary(
        context: Context,
        uri: Uri
    ): String {

        val inputStream: InputStream? =
            context.contentResolver.openInputStream(uri)

        val fileBytes =
            inputStream?.readBytes()
                ?: throw Exception("Failed to read image")

        val requestBody =
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    "profile.jpg",
                    RequestBody.create(
                        "image/*".toMediaTypeOrNull(),
                        fileBytes
                    )
                )
                .addFormDataPart(
                    "upload_preset",
                    uploadPreset
                )
                .build()

        val request = Request.Builder()
            .url(cloudinaryUrl)
            .post(requestBody)
            .build()

        val response =
            OkHttpClient().newCall(request).execute()

        if (!response.isSuccessful)
            throw Exception("Upload failed")

        val responseBody =
            response.body?.string()

        val secureUrl =
            Regex("\"secure_url\":\"(.*?)\"")
                .find(responseBody ?: "")
                ?.groupValues?.get(1)

        return secureUrl
            ?: throw Exception("Image URL not found")
    }
}