package com.deepseek.firstapp.screens.products

import android.R
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(navController: NavHostController,productID: String){
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var descriprition by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    //image picker
    val imagePickerLauncher= rememberLauncherForActivityResult(
        contract= ActivityResultContracts.GetContent()){
            uri:Uri? ->
        if (uri !=null) imageUri=uri
    }
    //fetch product data
    LaunchedEffect(productID) {
        
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Update Product",color=Color.Magenta) },
                colors= TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green
                )
            )
        }
    )
    {
        innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = productName,
                onValueChange = {productName=it},
                label = {Text("product name")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = price,
                onValueChange = {price=it},
                label = {Text(" product price")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = descriprition,
                onValueChange = {descriprition=it},
                label = {Text("product description")},
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(20.dp))
            //image preview (old  or new)
            val imageSource :Any?=imageUri ?: imageUrl
            if (imageSource != null) {
                AsyncImage(
                    model = imageSource,
                    contentDescription = "product",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
                //CHANGE IMAGE
                Button(onClick = {
                    imagePickerLauncher.launch("image/*")
                }) {
                    Text("UPDATE IMAGE")
                }
            Spacer(modifier = Modifier.height(20.dp))
            //UPDATE PRODUCT BUTTON
            Button(onClick = {
               //UPDATE PRODUCT LOGIC
            },
                modifier = Modifier.fillMaxWidth(),
                colors= ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor =Color.Blue
                )
                ) {
                Text("UPDATE PRODUCT")
            }









        }
    }

}
@Preview(showBackground = true)
@Composable
fun UpdateProductScreenPreview(){
    UpdateProductScreen(navController = rememberNavController(), productID = "2")

}