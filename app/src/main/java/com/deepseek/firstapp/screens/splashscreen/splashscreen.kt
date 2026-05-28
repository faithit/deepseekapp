package com.deepseek.firstapp.screens.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deepseek.firstapp.R
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.deepseek.firstapp.navigation.ROUTE_LOGIN
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay

@Composable
fun SplashScreen(navController: NavHostController){
    LaunchedEffect(true) {
        delay(2000) //2 seconds delay
        navController.navigate(ROUTE_LOGIN)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        val infiniteTransition = rememberInfiniteTransition(label = "")

        val offsetX by infiniteTransition.animateFloat(
            initialValue = -30f,
            targetValue = 30f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )

        val rotation by infiniteTransition.animateFloat(
            initialValue = -15f,
            targetValue = 15f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 700,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )
        Image(
            painter = painterResource(id=R.drawable.newlogo),
            contentDescription = "splash logo",
            modifier = Modifier
                .size(180.dp)
                .offset(x = offsetX.dp)
                .rotate(rotation)
        )

    }

}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(rememberNavController())
}