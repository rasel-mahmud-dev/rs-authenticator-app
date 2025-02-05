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
    if (!isAuthenticated) {
        LaunchedEffect(isAuthenticated) {
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
    } else {
        content?.invoke()
    }
}


@Composable
fun AuthExcludeRoute(
    isAuthenticated: Boolean,
    navController: NavHostController,
    content: @Composable (() -> Unit)? = null,
) {
    if (isAuthenticated) {
        LaunchedEffect(isAuthenticated) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    } else {
        content?.invoke()
    }
}