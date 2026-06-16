package com.deepseek.firstapp.screens.profile

import android.R.attr.text
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.deepseek.firstapp.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Custom Theme Colors matching the reference image
val AppDarkBackground = Color(0xFF0A1220)
val AppCardBackground = Color(0xFF142238)
val AppGreenAccent = Color(0xFF6BBB3C)
val AppGreenGradientEnd = Color(0xFF5A9E32)
val TextSecondary = Color(0xFF8A9AAB)
val DestructiveRed = Color(0xFFE54B4B)

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val profileVm = remember { UserProfileViewModel(context) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profileImage by remember { mutableStateOf("") }

    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var selectedTheme by remember { mutableStateOf("Dark") } // Theme state trac

    LaunchedEffect(Unit) {
        userId?.let {
            FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(it)
                .get()
                .addOnSuccessListener { snapshot ->
                    fullName = snapshot.child("fullname").value.toString()
                    email = snapshot.child("email").value.toString()
                    profileImage = snapshot.child("profileImage").value?.toString() ?: ""
                }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            profileVm.uploadProfilePicture(it)
        }
    }

    // Helper to get initials from fullname
    val initials = remember(fullName) {
        fullName.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .joinToString("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppDarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // 1. Top Section Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppGreenAccent)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Account",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White
                )
            }

            // Main Body Content Wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                // 2. Green Profile Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(AppGreenAccent, AppGreenGradientEnd)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBox,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "MY PROFILE",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }

                                // Edit Profile Button overlaying the card
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .clickable { launcher.launch("image/*") }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Picture",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Edit",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Dynamic Image Avatar Frame
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1B341B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (profileImage.isNotEmpty()) {
                                        AsyncImage(
                                            model = profileImage,
                                            contentDescription = "Profile Picture",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Text(
                                            text = initials.ifEmpty { "U" },
                                            color = AppGreenAccent,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = fullName.ifEmpty { "User Name" },
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = email.ifEmpty { "email@example.com" },
                                        color = Color.White.copy(alpha = 0.75f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Group: Account Management
                SectionTitle(title = "ACCOUNT MANAGEMENT")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCardBackground)
                ) {
                    Column {
                        ProfileMenuRow(
                            icon = Icons.Default.Phone,
                            iconTint = Color(0xFFE5A93C),
                            title = "Mobile Numbers",
                            subtitle = "Add, verify or remove numbers",
                            isLast = false
                        )

                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Group: Payment Methods
                SectionTitle(title = "PAYMENT METHODS")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCardBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.05f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "No payment method",
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        }

                        Button(
                            onClick = { /* Handle action */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22441F)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Add", color = AppGreenAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 5. Group: App Settings
                SectionTitle(title = "APP SETTINGS")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCardBackground)
                ) {
                    Column {
                        ProfileMenuRow(
                            icon = Icons.Default.AccountBox,
                            iconTint = Color(0xFF3C9BFF),
                            title = "Linked Accounts",
                            subtitle = "Sign in with Google or Apple",
                            isLast = false
                        )

                        // Row with Switch toggle element
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF382A16)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Color(0xFFE5A93C)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {

                                    Text(text = "Notifications", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "Receive push notifications", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                            var notificationsEnabled by remember { mutableStateOf(false) }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = AppGreenAccent,
                                    uncheckedThumbColor = Color.Gray,
                                    uncheckedTrackColor = Color(0xFF25354E)
                                )
                            )
                        }
                    }
                }

                // 5. Group: Appearance Section (From Image 1000739434.jpg)
                SectionTitle(title = "APPEARANCE")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCardBackground)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF382A16)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = Color(0xFFE5A93C)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(text = "Theme", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Segmented Theme Picker
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F1B2D), RoundedCornerShape(12.dp))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val options = listOf("System default", "Light", "Dark")
                            options.forEach { option ->
                                val isSelected = selectedTheme == option
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color(0xFF1B2C44) else Color.Transparent)
                                        .clickable { selectedTheme = option }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = option,
                                        color = if (isSelected) AppGreenAccent else TextSecondary,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                // 6. Group: Help & Info Section (From Image 1000739434.jpg)
                SectionTitle(title = "HELP & INFO")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCardBackground)
                ) {
                    Column {
                        ProfileMenuRow(
                            icon = Icons.Default.Info,
                            iconTint = AppGreenAccent,
                            title = "About Faiba",
                            subtitle = "Learn more about Faiba",
                            isLast = false
                        )
                        ProfileMenuRow(
                            icon = Icons.Default.Build,
                            iconTint = Color(0xFFE5A93C),
                            title = "Replay Feature Tour",
                            subtitle = "Learn how to navigate the app",
                            isLast = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 7. Group: Account Actions Section (From Image 1000739434.jpg)
                SectionTitle(title = "ACCOUNT ACTIONS")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCardBackground)
                ) {
                    Column {
                        // Logout Row
                        ProfileMenuRow(
                            icon = Icons.Default.ExitToApp,
                            iconTint = DestructiveRed,
                            title = "Logout",
                            subtitle = "Sign out of your account",
                            isLast = false,
                            isDestructive = true
                        )
                        // Delete Profile Row
                        ProfileMenuRow(
                            icon = Icons.Default.Delete,
                            iconTint = DestructiveRed,
                            title = "Delete Profile",
                            subtitle = "Remove login profile from this device",
                            isLast = true,
                            isDestructive = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
            }
        }



@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    isLast: Boolean,
    isDestructive: Boolean = false // Added flag for the red action items
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Handle Row Action */ }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Background Frame around the Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go",
                tint = TextSecondary.copy(alpha = 0.6f)
            )
        }

        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.06f),
                thickness = 1.dp
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun profileScreenPreview(){
    ProfileScreen(navController = rememberNavController())
}
