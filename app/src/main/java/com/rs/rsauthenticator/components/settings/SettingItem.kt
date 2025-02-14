package com.rs.rsauthenticator.components.settings


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsRow


@Composable
fun SettingItem(item: Item, navController: NavHostController) {
    RsRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},

        py = 20.dp
    ) {
        RsRow(
            px = 24.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,

            ){
            CustomText(icon = item.icon, fs = 18.sp, color = MaterialTheme.colorScheme.primary)
            CustomText(
                text = item.label,
                fs = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

}