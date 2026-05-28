package com.deepseek.firstapp.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.deepseek.firstapp.R
import com.deepseek.firstapp.navigation.ROUTE_DASHBOARD
import com.deepseek.firstapp.navigation.ROUTE_REGISTER

@Composable
fun LoginScreen(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Text("LOGIN",
            fontSize = 38.sp,
            color = Color.Green,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.newlogo),
            contentDescription = "logo",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value=email,
            onValueChange = {email=it},
            label = {Text("Enter email address")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true

        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value=password,
            onValueChange = {password=it},
            label = {Text("Enter password")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "password icon Icon"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
            )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
        //add login logic
            navController.navigate(ROUTE_DASHBOARD)
         },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.Blue)
        ) {
            Text("LOGIN", fontSize = 24.sp)

        }
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = {navController.navigate(ROUTE_REGISTER)}) {
            Text(
                text = "Don't have an account? Register here",
                fontSize = 20.sp
            )
        }




    }

}
@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}