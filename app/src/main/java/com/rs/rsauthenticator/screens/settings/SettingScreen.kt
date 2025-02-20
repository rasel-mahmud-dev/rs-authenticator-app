package com.rs.rsauthenticator.screens.settings


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsIconButton
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.settings.SettingItem
import com.rs.rsauthenticator.ui.theme.faBrand

data class Item(val label: String, val icon: String, val route: String)

val items = listOf(
    Item("Home", "\uf015", "home"), // Home icon
    Item("Profile", "\uf2bd", "settings/profile"), // User icon
//    Item("Settings", "\uf013", "settings/settings"), // Cog icon
    Item("Backup", "\uf233", "settings/backup-restore"), // Cloud Upload icon
    Item("Security", "\uf505", "settings/security"), // Shield icon
    Item("Trash", "\uf2ed", "settings/trash"), // Trash bin icon
    Item("About", "\uf05a", "settings/about"), // Info circle icon
    Item("Features", "\uf0eb", "settings/features"), // Lightbulb icon
    Item("Guide(Tour)", "\uf0eb", "tour"), // Lightbulb icon
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingScreen( navController: NavHostController) {
    val applicationContext = LocalContext.current
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
                modifier = Modifier.fillMaxWidth(),
                pt = 50.dp,
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                RsIconButton(modifier = Modifier
                    .clip(shape = CircleShape),
                    h = 50.dp,
                    w = 50.dp,
                    bgColor = Color(0x0C7C7C7D),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/rasel-mahmud-dev")
                        )
                        applicationContext.startActivity(intent)
                    }) {
                    CustomText(fs = 28.sp, icon = "\uf09b", fontFamily = faBrand)
                }

                RsIconButton(modifier = Modifier
                    .clip(shape = CircleShape),
                    h = 50.dp,
                    w = 50.dp,
                    bgColor = Color(0x0C4059F3),
                    onClick = {
                        val linkedInAppUri = Uri.parse("linkedin://profile/rasel-mahmud-dev")
                        val webUri = Uri.parse("https://www.linkedin.com/in/rasel-mahmud-dev")

                        try {
                            val intent = Intent(Intent.ACTION_VIEW, linkedInAppUri)
                            applicationContext.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                            applicationContext.startActivity(webIntent)
                        }
                    }) {
                    CustomText(
                        fs = 28.sp,
                        color = Color.Blue,
                        icon = "\uf0e1",
                        fontFamily = faBrand
                    )
                }

                RsIconButton(modifier = Modifier
                    .clip(shape = CircleShape),
                    h = 50.dp,
                    w = 50.dp,
                    bgColor = Color(0x0C4059F3),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/rasel.mahmud.dev/")
                        )
                        applicationContext.startActivity(intent)
                    }) {
                    CustomText(
                        fs = 28.sp,
                        color = Color.Blue,
                        icon = "\uf09a",
                        fontFamily = faBrand
                    )
                }


            }

        }
    }
}
