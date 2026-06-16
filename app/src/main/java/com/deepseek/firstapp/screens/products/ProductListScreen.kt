package com.deepseek.firstapp.screens.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.deepseek.firstapp.models.Product
import com.deepseek.firstapp.R
import com.deepseek.firstapp.navigation.ROUTE_ADDPRODUCT
import com.deepseek.firstapp.navigation.ROUTE_UPDATEPRODUCT
import com.deepseek.firstapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavHostController){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title={Text("PRODUCT LIST",
                    fontWeight = FontWeight.Bold,
                    color=Color.White
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Green
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate(ROUTE_ADDPRODUCT)},
                containerColor = Color.Green
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add product icon",
                    tint=Color.White
                )
            }
        }
    ) {
        innerpadding ->
        var product= remember { mutableStateOf(Product("","","","","")) }
        var products =remember { mutableStateListOf<Product>() }

        var context= LocalContext.current
        var myproductviewmodel= ProductViewModel(navController,context)
        //fetch products from firebase
        LaunchedEffect(Unit) {
            myproductviewmodel.allProducts(product,products)
        }
        LazyColumn(
            modifier =  Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            items(products){item ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = "product",
//                        placeholder = painterResource(R.drawable.newlogo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                    Text(
                       text=item.name,
                       style= MaterialTheme.typography.titleLarge,
                       color = Color.Magenta)
                    Text(text=item.description,
                        fontSize = 18.sp,
                    )
                    Text(text="Price : KES ${item.price}",
                        color=Color.Red,
                        fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        //delete button
                        OutlinedButton(
                            onClick = {
                                myproductviewmodel.deleteProduct(item.id)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Delete",
                                fontWeight = FontWeight.Bold
                            )
                        }
//                        Text(
//                            text = "DELETE",
//                             color= MaterialTheme.colorScheme.error,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.clickable{
//                                myproductviewmodel.deleteProduct(item.id)
//                            }
//                        )
                        //update
//                        Text(text = "UPDATE",
//                            color= Color.Green,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.clickable{
//                                //update logic
//                        navController.navigate("$ROUTE_UPDATEPRODUCT/${item.id}")
//                            }
//                        )
                        // Update Button
                        Button(
                            onClick = {
                             navController.navigate("$ROUTE_UPDATEPRODUCT/${item.id}")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Text(
                                text = "Update",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    }


                }

            }

        }
    }

}
@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview(){
    ProductListScreen(navController = rememberNavController())
}
