package com.rs.rsauthenticator.screens.security

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.state.AppState
import com.rs.rsauthenticator.ui.theme.AppColors
import com.rs.rsauthenticator.ui.theme.Primary40


data class SecurityItem(
    val title: String,
    val description: String,
    val action: String,  // "switch" or "navigate"
    val isEnabled: Boolean,
    val routePath: String
)


@Composable
fun SecurityScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val db = AppStateDbHelper.getInstance(context)


    val securityItems = listOf(
        SecurityItem(
            "Pin Lock",
            "Unlock the app with a 6-digit PIN. To use a PIN, your device must have a screen lock.",
            "switch",
            AppState.isLocked,
            ""
        ),
        SecurityItem(
            "Change PIN",
            "Use your device-specific PIN to unlock the app.",
            "navigate",
            false,
            "settings/security/setpin"
        )
    )

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Security",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->


            RsColumn(modifier = Modifier.padding(padding)) {

                RsColumn(pb = 5.dp, pt = 30.dp, pl = 16.dp) {
                    CustomText(
                        text = "APPLICATION LOCK",
                        fs = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = AppColors.Dark10
                    )
                }

                securityItems.forEach { item ->
                    SecurityItemRow(item, navHostController) { newState ->
                        if (item.title == "Pin Lock") {
                            AppState.updateLockState(newState)
                            db.setPinEnabled(newState)
                        }
                    }
                }
            }
        })
}


@Composable
fun SecurityItemRow(
    item: SecurityItem,
    navHostController: NavHostController,
    onToggleChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                println(item)
                if (item.action == "navigate") {
                    navHostController.navigate(item.routePath)
                }
            }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {

            CustomText(
                text = item.title,
                fs = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                pb = 5.dp
            )

            Text(
                text = item.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        if (item.action == "switch") {
            Switch(
                modifier = Modifier.scale(0.8F),
                checked = item.isEnabled,
                onCheckedChange = onToggleChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Primary40,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.DarkGray
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewSecuritySettingsScreen() {
    val navController = NavHostController(context = LocalContext.current)
    SecurityScreen(navHostController = navController)
}
