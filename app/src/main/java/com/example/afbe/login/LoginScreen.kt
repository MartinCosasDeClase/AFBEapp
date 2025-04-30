package com.example.afbe.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.afbe.R

@Composable
fun LoginScreen(){
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)){
        Login(Modifier.align(Alignment.Center))
    }
}
@Composable
fun Login(modifier: Modifier){
    Column(modifier = modifier) {
        HeaderImage()
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField()
    }
}

@Composable
fun EmailField(){
    TextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth())
}

@Composable
fun HeaderImage(){
    Image(painter = painterResource(id = R.drawable.afbelogo), contentDescription = "Header")
}