package com.deepseek.firstapp.screens.dashboard

import AuthViewModel
import android.icu.text.CaseMap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.deepseek.firstapp.R
import com.deepseek.firstapp.navigation.ROUTE_ADDPRODUCT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController){
    val context= LocalContext.current
    val myauth=AuthViewModel(navController,context)
    Scaffold(
        //TOP bar
        topBar = {
           TopAppBar(
                title = {Text("my dashboard")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green,
                    titleContentColor = Color.Blue),
               actions={
                   IconButton(onClick = {}){
                       Icon(Icons.Default.Settings,
                           contentDescription = "settings icon")
                   }
                   IconButton(onClick = {}){
                       Icon(Icons.Default.Person,
                           contentDescription = "person icon")
                   }
                   IconButton(onClick = {
                     myauth.logout()
                        }){
                       Icon(Icons.Default.ExitToApp,
                           contentDescription = " logout icon ")
                   }
               }
            )
        },
        //bottombar
        bottomBar = {
//            BottomAppBar(
//                containerColor = Color.Green,
//                contentColor = Color.Blue) {
//                Text("bottom bar")
//            }
            NavigationBar(
                containerColor = Color.Green,
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Home,
                            contentDescription ="home icon")
                    },
                    label = {Text("Home")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Settings,
                            contentDescription ="settings icon")
                    },
                    label = {Text("Settings")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Person,
                            contentDescription ="person icon")
                    },
                    label = {Text("Profile")}
                )
            }
        },
        //floating action button
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "add icon")
            }
        }
    )
    {
        innerpadding ->
        Column(
            modifier = Modifier
                .padding(innerpadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var username by remember { mutableStateOf("loading ...") }
            LaunchedEffect(Unit) {
                myauth.getCurrentUserName { username=it }
            }
            //surface
            Surface(
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id=R.drawable.banner),
                    contentDescription = "banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(20.dp) )
            Text(text="welcome  $username")
            Text("welcome to admin dashboard",
                color = Color(0xFF2E7D32),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DashboardCard(
                        title = "ADD PRODUCT",
                        onClick = {navController.navigate(ROUTE_ADDPRODUCT)}
                    )
                    DashboardCard(
                        title = "PRODUCTS",
                        onClick = {}
                    )
                }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DashboardCard(
                    title = "DEEPSEEK",
                    onClick = {}
                )
                DashboardCard(
                    title = "PROFILE",
                    onClick = {}
                )
            }


        }
    }
}
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview(){
    DashboardScreen(rememberNavController())

}
//dashboard card
@Composable
fun DashboardCard(
    title:String,
    onClick:()->Unit
){
    Card (
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp) ,
//        color=Color.Yellow,
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .clickable{onClick()}
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text=title,
                style= MaterialTheme.typography.titleMedium,
                color=Color(0XFF2E7D32))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DashboardCardPreview(){
    DashboardCard(
        title = "Deepseeksss",
        onClick = {}
    )

}
