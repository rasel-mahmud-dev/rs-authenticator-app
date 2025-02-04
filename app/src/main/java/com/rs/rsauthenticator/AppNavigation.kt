package com.rs.rsauthenticator

import android.content.Context

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.rs.rsauthenticator.screens.HomeScreen
import com.rs.rsauthenticator.screens.ForgotPasswordScreen
import com.rs.rsauthenticator.screens.LoginScreen
import com.rs.rsauthenticator.screens.RegistrationScreen


@Composable
fun AppNavigation(context: Context, navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(context, navController)
        }

        composable("about") {
            AboutScreen(context, navController)
        }
        composable("login") {
            LoginScreen(context, navController)
        }
        composable("registration") {
            RegistrationScreen(context, navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(context, navController)
        }
        composable("appDetails/{appName}") { backStackEntry ->
            val appName = backStackEntry.arguments?.getString("appName")
//            AppDetailsScreen(appName ?: "Unknown") // Navigate to App Details Screen
        }
    }
}
