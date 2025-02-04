package com.rs.rsauthenticator.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController

@Composable
fun ProtectedRoute(
    isAuthenticated: Boolean,
    navController: NavHostController,
    content: @Composable (() -> Unit)? = null,
) {
    if (isAuthenticated) {
        LaunchedEffect(Unit) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    } else {
        content?.invoke()
    }
}