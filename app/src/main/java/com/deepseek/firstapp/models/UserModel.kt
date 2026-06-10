package com.deepseek.firstapp.models

data class User(
        var fullname: String="",
        var email: String="",
        var password: String="",
        var userID:String="",
        var profileImage: String = ""
)