package com.rs.rsauthenticator.components.settings


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsIconButton
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.ui.theme.faBrand

data class Item(val label: String, val icon: String, val route: String)

val items = listOf(
    Item("Home", "\uf015", "home"), // Home icon
    Item("Profile", "\uf2bd", "settings/profile"), // User icon
//    Item("Settings", "\uf013", "settings/settings"), // Cog icon
    Item("Backup", "\uf233", "settings/backup"), // Cloud Upload icon
    Item("Security", "\uf505", "settings/security"), // Shield icon
    Item("Trash", "\uf2ed", "settings/trash"), // Trash bin icon
    Item("About", "\uf05a", "settings/about"), // Info circle icon
    Item("Features", "\uf0eb", "settings/features"), // Lightbulb icon
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingScreen(applicationContext: Context, navController: NavHostController) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RsRow(px = 16.dp, pt = 16.dp) {
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            LazyColumn {
                items(items) { item ->
                    SettingItem(item, navController)
                }
            }

            RsRow(
                pt = 50.dp,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                RsIconButton(modifier = Modifier
                    .clip(shape = CircleShape),
                    h = 50.dp,
                    w = 50.dp,
                    onClick = {}) {
                    CustomText(fs = 28.sp, icon = "\uf09b", fontFamily = faBrand)
                }

                RsIconButton(modifier = Modifier
                    .clip(shape = CircleShape),
                    h = 50.dp,
                    w = 50.dp,
                    onClick = {}) {
                    CustomText(fs = 28.sp, color = Color.Blue, icon = "\uf09a", fontFamily = faBrand)
                }
            }

        }
    }
}
