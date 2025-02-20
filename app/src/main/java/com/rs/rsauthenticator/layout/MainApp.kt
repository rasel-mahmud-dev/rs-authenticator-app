package com.rs.rsauthenticator.layout

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.navigation.compose.rememberNavController
import com.rs.rsauthenticator.AppNavigation


@Composable
fun MainApp(applicationContext: Context) {

    val navController = rememberNavController()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF0303))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {

            AppNavigation(applicationContext, navController)
        }
    }
}


