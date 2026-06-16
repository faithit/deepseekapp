package com.deepseek.firstapp.models

import com.deepseek.firstapp.R

data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: Int
)
val onboardingItems=listOf(
    OnboardingItem(
        title="welcome to  keto mart",
        description = "get to descover amazing products",
        imageRes=R.drawable.newlogo
    ),
    OnboardingItem(
        title = "Discover amazing products",
        description = "get to browse thousand of amazing products",
        imageRes =R.drawable.logob
    ),
    OnboardingItem(
            title = "fast and secure delivery",
             description = "enjoy seamless ordering and delivering of products",
             imageRes =R.drawable.logoc
)


)