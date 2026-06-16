package com.deepseek.firstapp.screens.User

import AuthViewModel
import android.R
import android.R.attr.label
import android.graphics.drawable.Icon
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.deepseek.firstapp.models.Product
import com.deepseek.firstapp.navigation.ROUTE_ADDPRODUCT
import com.deepseek.firstapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(navController: NavHostController){
    var context= LocalContext.current
    var productViewModel= ProductViewModel(navController,context)
    var authViewModel=AuthViewModel(navController,context)
    var searchText by remember { mutableStateOf("") }
    var product=remember { mutableStateOf(Product("","","","","") )}
    var products=remember { mutableStateListOf<Product>()}
    var username by remember { mutableStateOf("loading ...") }
    //fetch produuts from firebase
        LaunchedEffect(Unit) {
            productViewModel.allProducts(product, products)
            authViewModel.getCurrentUserName {username=it  }
        }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("KetoMart")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green),
                actions={
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "cart"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "cart"
                        )
                    }
                    IconButton(onClick = {authViewModel.logout()}) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "cart")
                    }
                }
                )
                 },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = Color.Green
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "CART",
                    tint=Color.White
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Green
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                      Icon(
                          Icons.Default.Home,
                          contentDescription = "home")
                    },
                    label={Text("Home")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "profile")
                    },
                    label={Text("Profile")}
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "CART")
                    },
                    label={Text("Cart")}
                )
            }
        }

    ) {
        innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(16.dp)
        ) {
            Text(
                text="welcome  ${username}!",
                color=Color.Magenta,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            //search bar
            OutlinedTextField(
                value = searchText,
                onValueChange = {searchText=it},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {Text("search products ..")},
                leadingIcon = {Icon(imageVector = Icons.Default.Search,
                    contentDescription = "search")},
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text="featured products",
                    style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products){product->
                    ProductCard(product)
                }
            }
        }
    }
}
@Composable
fun ProductCard(product: Product){
    Card(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = "product",
                contentScale = ContentScale.Crop,
                modifier= Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            //name
            Text(text=product.name,
                fontSize = 18.sp)
            //price
            Text(
                text=" ksh ${product.price}",
                color=Color.Red
                )
            //descr
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserDashboardPreview(){
    UserDashboardScreen(navController = rememberNavController())

}