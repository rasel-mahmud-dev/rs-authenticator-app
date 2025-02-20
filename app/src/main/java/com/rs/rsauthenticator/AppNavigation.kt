package com.rs.rsauthenticator

import android.content.Context

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rs.rsauthenticator.components.AuthExcludeRoute
import com.rs.rsauthenticator.components.ProtectedRoute
import com.rs.rsauthenticator.components.security.PinSetupScreen
import com.rs.rsauthenticator.components.settings.SettingScreen
import com.rs.rsauthenticator.layout.HomeLayout
import com.rs.rsauthenticator.screens.AboutScreen
import com.rs.rsauthenticator.screens.AppsScreen
import com.rs.rsauthenticator.screens.ConnectAppScreen
import com.rs.rsauthenticator.screens.ForgotPasswordScreen
import com.rs.rsauthenticator.screens.HomeScreen2
import com.rs.rsauthenticator.screens.LoginScreen
import com.rs.rsauthenticator.screens.RegistrationScreen
import com.rs.rsauthenticator.screens.BackupRestore
import com.rs.rsauthenticator.screens.FeaturesScreen
import com.rs.rsauthenticator.screens.ProfileScreen
import com.rs.rsauthenticator.screens.SecurityScreen
import com.rs.rsauthenticator.screens.TourScreen
import com.rs.rsauthenticator.screens.TrashScreen
import com.rs.rsauthenticator.state.AuthState
import com.rs.rsauthenticator.state.SharePref


@Composable
fun AppNavigation(context: Context, navController: NavHostController) {

    val isAuthenticated = AuthState.auth != null

    val sharePref = SharePref.getInstance(context)
    val isInit = sharePref.isAppInitialized()

    NavHost(
        navController = navController,
//        startDestination = if (isAuthenticated) "home" else "login"
//        startDestination = "settings/security"
        startDestination = "settings/security/setpin"
//        startDestination = if (!isInit) "tour" else "home"
    ) {

        composable("home") {
            HomeLayout(navController) {
                HomeScreen2(navController)
            }
        }

        composable("settings") {
            HomeLayout(navController) {
                SettingScreen(navController)
            }
        }

        composable("apps") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                AppsScreen(navController)
            }
        }

        composable("connect_app") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                ConnectAppScreen(navController)
            }
        }

        composable("capture_qr") {
            ProtectedRoute(isAuthenticated = isAuthenticated, navController = navController) {
                ConnectAppScreen(navController)
            }
        }

        composable("settings/about") {
            AboutScreen(navController)
        }

        composable("settings/features") {
            FeaturesScreen(navController)
        }

        composable("settings/profile") {
            ProfileScreen(navController)
        }

        composable("settings/security") {
            SecurityScreen(navController)
        }

        composable("settings/security/setpin") {
            PinSetupScreen(navController)
        }

        composable("settings/trash") {
            TrashScreen(navController)
        }

        composable("settings/backup-restore") {
            BackupRestore(navController)
        }

        composable("login") {
            AuthExcludeRoute(isAuthenticated = isAuthenticated, navController = navController) {
                LoginScreen(navController)
            }
        }

        composable("registration") {
            AuthExcludeRoute(isAuthenticated = isAuthenticated, navController = navController) {
                RegistrationScreen(navController)
            }
        }

        composable("forgot_password") {
            AuthExcludeRoute(isAuthenticated = isAuthenticated, navController = navController) {
                ForgotPasswordScreen(navController)
            }
        }

        composable("tour") {
            TourScreen(navController)
        }

    }
}
