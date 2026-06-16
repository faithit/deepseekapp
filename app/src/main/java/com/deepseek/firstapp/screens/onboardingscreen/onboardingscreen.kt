package com.deepseek.firstapp.screens.onboarding

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.deepseek.firstapp.R
import com.deepseek.firstapp.models.onboardingItems
import com.deepseek.firstapp.navigation.ROUTE_LOGIN
import com.deepseek.firstapp.navigation.ROUTE_ONBOARDING
import kotlinx.coroutines.launch



@Composable
fun OnboardingScreen(
    navController: NavHostController
) {
    val pagerState = rememberPagerState(
        pageCount = { onboardingItems.size }
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //show skip if not in the last page
        if (pagerState.currentPage != onboardingItems.lastIndex) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_ONBOARDING) {
                            inclusive = true
                        }
                    }
                }) {
                    Text("skip")
                }
            }
        } else {
            Spacer(modifier = Modifier.height(48.dp))
        }
        //slides
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val item=onboardingItems[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                    Image(
                        painter=painterResource(id=item.imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                Spacer(modifier= Modifier.height(20.dp) )
                Text(text=item.title,
                    color=Color.Green,
                    style= MaterialTheme.typography.headlineMedium)
                Text(
                    text=item.description,
                    color=Color.Blue,
                    style= MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier= Modifier.height(20.dp) )
        //indicatotrs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardingItems.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(
                            if (isSelected) 12.dp else 8.dp
                        )
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.Green else Color.LightGray
                        )
                )
            }
        }

     //get started button
        Button(onClick = {
            if (pagerState.currentPage==onboardingItems.lastIndex){
                navController.navigate(ROUTE_LOGIN){
                    popUpTo(ROUTE_ONBOARDING){inclusive=true}
                }
            }else{
                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage+1) }
            }
        },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                     contentColor = Color.Blue)
        ) {
            Text(text=if (pagerState.currentPage==onboardingItems.lastIndex)"Get started" else "Next")
        }
        Spacer(modifier= Modifier.height(60.dp) )

    }
}
@Preview(showBackground = true)
@Composable
fun onboardingscreenpreview(){
    OnboardingScreen(navController = rememberNavController())
}