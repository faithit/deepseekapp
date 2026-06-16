package com.deepseek.firstapp.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deepseek.firstapp.screens.User.UserDashboardScreen
import com.deepseek.firstapp.screens.dashboard.DashboardScreen
import com.deepseek.firstapp.screens.login.LoginScreen
import com.deepseek.firstapp.screens.onboarding.OnboardingScreen

import com.deepseek.firstapp.screens.products.AddProductScreen
import com.deepseek.firstapp.screens.products.ProductListScreen
import com.deepseek.firstapp.screens.products.UpdateProductScreen
import com.deepseek.firstapp.screens.profile.ProfileScreen
import com.deepseek.firstapp.screens.register.RegisterScreen
import com.deepseek.firstapp.screens.splashscreen.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier=Modifier,
    navController: NavHostController= rememberNavController(),
    startDestination: String=ROUTE_SPLASHSCREEN
){
    NavHost(
        navController=navController,
        modifier=modifier,
        startDestination = startDestination
    ){
        composable(ROUTE_SPLASHSCREEN) {
            SplashScreen(navController)
        }
        composable(ROUTE_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUTE_ADDPRODUCT) {
            AddProductScreen(navController)
        }
        composable(ROUTE_USERPROFILE) {
            ProfileScreen(navController)
        }
        composable (ROUTE_PRODUCTLIST){
            ProductListScreen(navController)
        }
        composable (ROUTE_UPDATEPRODUCT+ "/{productId}"){backStackEntry ->
            val productId=backStackEntry.arguments?.getString("productId")!!
            UpdateProductScreen(navController,productId)
        }
        composable (ROUTE_USERDASHBOARD){
            UserDashboardScreen(navController)
        }
        composable(ROUTE_ONBOARDING) {
            OnboardingScreen(navController)
        }



    }

}