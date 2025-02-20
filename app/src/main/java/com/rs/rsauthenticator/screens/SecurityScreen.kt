package com.rs.rsauthenticator.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.database.AppStateDbHelper
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

    var pin by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPin by remember { mutableStateOf(TextFieldValue("")) }
    var savedPin by remember { mutableStateOf(db.getPin()) }
    var step by remember { mutableIntStateOf(1) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isPinEnabled by rememberSaveable { mutableStateOf(false) }

    val securityItems = listOf(
        SecurityItem(
            "Pin Lock",
            "Unlock the app with a 6-digit PIN. To use a PIN, your device must have a screen lock.",
            "switch",
            isPinEnabled,
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

            RsColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomText(
                    text = when {
                        step == 1 -> "Set Your PIN"
                        else -> "Confirm Your PIN"
                    },
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fs = 24.sp,
                    style = MaterialTheme.typography.headlineMedium
                )

                securityItems.forEach { item ->
                    SecurityItemRow(item, navHostController) { newState ->
                        if (item.title == "Pin Lock") {
                            isPinEnabled = newState
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
            .padding(vertical = 12.dp, horizontal = 12.dp),
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
