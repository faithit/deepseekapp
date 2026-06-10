package com.deepseek.firstapp.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import coil.util.CoilUtils
import com.deepseek.firstapp.models.Product
import com.deepseek.firstapp.navigation.ROUTE_PRODUCTLIST
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream
import kotlin.jvm.java

class ProductViewModel ( var navController: NavHostController,var context: Context) {
    var cloudinaryUrl = "https://api.cloudinary.com/v1_1/dojp0mlml/upload" //do...use own cloud name
    var uploadPreset = "newproducts"
    val databasareference = FirebaseDatabase.getInstance().getReference("Products")

    //functions
    //crud c-create,r-read,u-update,d-delete
    //upload product  to firebase function
    fun uploadProduct(imageUri: Uri?, name: String, price: String, description: String) {
        val ref = databasareference.push()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageUrl = if (imageUri != null) {
                    uploadToCloudinary(context, imageUri)
                } else {
                    ""
                }
                val productData = mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "price" to price,
                    "description" to description,
                    "userId" to userId,
                    "imageUrl" to imageUrl
                )
                ref.setValue(productData).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "product added successfully", Toast.LENGTH_LONG)
                            .show()
                        navController.navigate(ROUTE_PRODUCTLIST)

                    } else {
                        Toast.makeText(context, "Error:${it.exception?.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Upload failed ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



        //upload image to cloudinary function using okthttp
        //extracts the secure image url from the response and returns the url
        // so we can save the url in firebase
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
        //fetch all products from firebase realtime database
    fun allProducts(
            product: MutableState<Product>,
            products: SnapshotStateList<Product>
        ): SnapshotStateList<Product> {
            databasareference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    products.clear()
                    for (snap in snapshot.children) {
                        val retrievedProduct = snap.getValue(Product::class.java)
                        if (retrievedProduct != null) {
                            product.value = retrievedProduct
                            products.add(retrievedProduct)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show()
                }
            })
            return products
        }
        //delete product function
    //delete product from database
        fun deleteProduct(productId: String){
            databasareference.child(productId).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context,"product deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context,"Failed to delete product", Toast.LENGTH_SHORT).show()
                }

        }
    //update product function
    //update existing product in firebase
    fun updateProduct(
        productId: String,
        name:   String,
        price: String,
        description: String,
        imageUri: Uri?
    ){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newImageUrl=imageUri?.let{ uploadToCloudinary(context,it) }
                val currentUser= FirebaseAuth.getInstance().currentUser
                val userId=currentUser?.uid ?:""
                val updates=mutableMapOf<String, Any>(
                    "Id" to productId,
                    "name" to name,
                    "price" to price,
                    "description" to description,
                    "userId" to userId
                )
                if(!newImageUrl.isNullOrEmpty()){
                    updates["imageUrl"]=newImageUrl
                }
                databasareference.child(productId).updateChildren(updates).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Product updated successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_PRODUCTLIST)
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"update failed : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
