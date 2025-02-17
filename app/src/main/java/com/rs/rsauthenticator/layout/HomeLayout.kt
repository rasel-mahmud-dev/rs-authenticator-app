package com.rs.rsauthenticator.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.HomeBottomNav
import com.rs.rsauthenticator.components.RsColumn

@Composable
fun HomeLayout(navHostController: NavHostController, content: @Composable () -> Unit) {

    var currentRoute by remember {
        mutableStateOf(
            navHostController.currentBackStackEntry?.destination?.route ?: "home"
        )
    }

    LaunchedEffect(navHostController) {
        navHostController.currentBackStackEntryFlow.collect { entry ->
            currentRoute = entry.destination.route ?: "home"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        content.invoke()
        RsColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            HomeBottomNav(activeTab = currentRoute, onChangeTab = {
                navHostController.navigate(it)
            })
        }
    }
}