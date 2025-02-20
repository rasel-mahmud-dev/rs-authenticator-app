package com.rs.rsauthenticator

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rs.rsauthenticator.components.AuthExcludeRoute
import com.rs.rsauthenticator.components.ProtectedRoute
import com.rs.rsauthenticator.screens.security.PinSetupScreen
import com.rs.rsauthenticator.screens.settings.SettingScreen
import com.rs.rsauthenticator.layout.HomeLayout
import com.rs.rsauthenticator.screens.common.AboutScreen
import com.rs.rsauthenticator.screens.ConnectAppScreen
import com.rs.rsauthenticator.screens.auth.ForgotPasswordScreen
import com.rs.rsauthenticator.screens.HomeScreen2
import com.rs.rsauthenticator.screens.auth.LoginScreen
import com.rs.rsauthenticator.screens.auth.RegistrationScreen
import com.rs.rsauthenticator.screens.settings.BackupRestore
import com.rs.rsauthenticator.screens.common.FeaturesScreen
import com.rs.rsauthenticator.screens.profile.ProfileScreen
import com.rs.rsauthenticator.screens.security.SecurityScreen
import com.rs.rsauthenticator.screens.guide.TourScreen
import com.rs.rsauthenticator.screens.settings.TrashScreen
import com.rs.rsauthenticator.state.AppState
import com.rs.rsauthenticator.state.SharePref


@Composable
fun AppNavigation(navController: NavHostController) {

    val isAuthenticated = AppState.auth != null

    val sharePref = SharePref.getInstance(LocalContext.current)
    val isInit = sharePref.isAppInitialized()

    NavHost(
        navController = navController,
//        startDestination = if (isAuthenticated) "home" else "login"
//        startDestination = "settings/security"
//        startDestination = "connect_app"
        startDestination = "login"
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
