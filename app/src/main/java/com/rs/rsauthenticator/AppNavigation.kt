package com.rs.rsauthenticator

import android.content.Context

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rs.rsauthenticator.components.AuthExcludeRoute
import com.rs.rsauthenticator.components.ProtectedRoute
import com.rs.rsauthenticator.screens.AppsScreen
import com.rs.rsauthenticator.screens.ConnectAppScreen
import com.rs.rsauthenticator.screens.ForgotPasswordScreen
import com.rs.rsauthenticator.screens.HomeScreen2
import com.rs.rsauthenticator.screens.LoginScreen
import com.rs.rsauthenticator.screens.RegistrationScreen
import com.rs.rsauthenticator.screens.Settings.BackupRestore
import com.rs.rsauthenticator.screens.Settings.FeaturesScreen
import com.rs.rsauthenticator.screens.Settings.ProfileScreen
import com.rs.rsauthenticator.screens.Settings.SecurityScreen
import com.rs.rsauthenticator.state.AuthState


@Composable
fun AppNavigation(context: Context, navController: NavHostController) {

    val isAuthenticated = AuthState.auth != null

    NavHost(
        navController = navController,
//        startDestination = if (isAuthenticated) "home" else "login"
        startDestination = "home"
//        startDestination = "settings/about"
//        startDestination = "settings/security"
    ) {

        composable("home") {
            HomeScreen2(context, navController)
        }

        composable("apps") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                AppsScreen(context, navController)
            }
        }

        composable("connect_app") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                ConnectAppScreen(context, navController)
            }
        }

        composable("capture_qr") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                ConnectAppScreen(context, navController)
            }
        }

        composable("settings/about") {
            AboutScreen(context, navController)
        }

        composable("settings/features") {
            FeaturesScreen(context, navController)
        }

        composable("settings/profile") {
            ProfileScreen(context, navController)
        }

        composable("settings/security") {
            SecurityScreen(context, navController)
        }

        composable("settings/trash") {
            TrashScreen(context, navController)
        }

        composable("settings/backup-restore") {
            BackupRestore(context, navController)
        }

        composable("login") {
            AuthExcludeRoute(isAuthenticated = isAuthenticated, navController = navController) {
                LoginScreen(context, navController)
            }
        }

        composable("registration") {
            AuthExcludeRoute(isAuthenticated = isAuthenticated, navController = navController) {
                RegistrationScreen(context, navController)
            }
        }

        composable("forgot_password") {
            AuthExcludeRoute(isAuthenticated = isAuthenticated, navController = navController) {
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
