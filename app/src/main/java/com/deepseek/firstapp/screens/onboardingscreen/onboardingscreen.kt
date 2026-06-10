package com.deepseek.firstapp.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.deepseek.firstapp.R
import com.deepseek.firstapp.navigation.ROUTE_LOGIN
import com.deepseek.firstapp.navigation.ROUTE_ONBOARDING
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.newlogo,
            title = "Discover Amazing Products",
            description = "Browse thousands of premium products curated just for you."
        ),
        OnboardingPage(
            imageRes = R.drawable.newlogo,
            title = "Compare Before You Buy",
            description = "Read reviews, compare features, and choose with confidence."
        ),
        OnboardingPage(
            imageRes = R.drawable.newlogo,
            title = "Fast & Secure Checkout",
            description = "Enjoy seamless ordering and secure payments anytime."
        )
    )

    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        navController.navigate(ROUTE_LOGIN) {
                            popUpTo(ROUTE_ONBOARDING) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text(
                        text = "Skip",
                        color = Color(0xFF4F46E5)
                    )
                }
            }

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->

                val item = pages[page]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(item.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = item.title,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = item.description,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Page Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                repeat(pages.size) { index ->

                    val selected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(
                                if (selected) 30.dp else 8.dp
                            )
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selected)
                                    Color(0xFF4F46E5)
                                else
                                    Color.LightGray
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Next / Get Started Button
            Button(
                onClick = {
                    scope.launch {

                        if (pagerState.currentPage < pages.lastIndex) {

                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )

                        } else {

                            navController.navigate(ROUTE_LOGIN) {
                                popUpTo(ROUTE_ONBOARDING) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(58.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F46E5)
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.lastIndex)
                        "Get Started"
                    else
                        "Next",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}