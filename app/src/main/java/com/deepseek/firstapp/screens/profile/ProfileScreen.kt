package com.deepseek.firstapp.screens.profile



import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.deepseek.firstapp.viewmodel.UserProfileViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun ProfileScreen(
    navController: NavHostController
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    val profileVm = remember {
        UserProfileViewModel(context)
    }

    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var profileImage by remember {
        mutableStateOf("")
    }

    val userId =
        FirebaseAuth.getInstance()
            .currentUser?.uid

    LaunchedEffect(Unit) {

        userId?.let {

            FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(it)
                .get()
                .addOnSuccessListener { snapshot ->

                    fullName =
                        snapshot.child("fullname")
                            .value.toString()

                    email =
                        snapshot.child("email")
                            .value.toString()

                    profileImage =
                        snapshot.child("profileImage")
                            .value?.toString() ?: ""
                }
        }
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) { uri ->

            imageUri = uri

            profileVm.uploadProfilePicture(uri)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        if (profileImage.isNotEmpty()) {

            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )

        } else {

            AsyncImage(
                model = "https://via.placeholder.com/150",
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Change Profile Picture")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}