package com.rs.rsauthenticator

import android.content.Context

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rs.rsauthenticator.components.ProtectedRoute
import com.rs.rsauthenticator.screens.AppsScreen
import com.rs.rsauthenticator.screens.ConnectAppScreen

import com.rs.rsauthenticator.screens.HomeScreen
import com.rs.rsauthenticator.screens.ForgotPasswordScreen
import com.rs.rsauthenticator.screens.HomeScreen2
import com.rs.rsauthenticator.screens.LoginScreen
import com.rs.rsauthenticator.screens.RegistrationScreen
import com.rs.rsauthenticator.state.AuthState


@Composable
fun AppNavigation(context: Context, navController: NavHostController) {

    val isAuthenticated = AuthState.auth != null

    NavHost(
        navController = navController,
//        startDestination = if (isAuthenticated) "home" else "login"
        startDestination = "home"
    ) {

        composable("home") {
            if (isAuthenticated) {
                HomeScreen2(context, navController)
            } else {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        composable("apps") {
            if (isAuthenticated) {
                AppsScreen(context, navController)
            } else {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        composable("connect_app") {
            if (isAuthenticated) {
                ConnectAppScreen(context, navController)
            } else {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
        composable("capture_qr") {
            if (isAuthenticated) {
                ConnectAppScreen(context, navController)
            } else {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        composable("about") {
            AboutScreen(context, navController)
        }

        composable("login") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                LoginScreen(context, navController)
            }
        }

        composable("registration") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                RegistrationScreen(context, navController)
            }
        }

        composable("forgot_password") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                ForgotPasswordScreen(context, navController)
            }
        }

        composable("appDetails/{appName}") { backStackEntry ->
            val appName = backStackEntry.arguments?.getString("appName")
            if (isAuthenticated) {
//                AppDetailsScreen(appName ?: "Unknown")
            } else {
                // If not authenticated, navigate to login screen
                navController.navigate("login") {
                    popUpTo("appDetails") { inclusive = true }
                }
            }
        }
    }
}
