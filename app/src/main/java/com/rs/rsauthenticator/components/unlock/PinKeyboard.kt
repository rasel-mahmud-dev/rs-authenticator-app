package com.rs.rsauthenticator.components.unlock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.ui.theme.AppColors
import com.rs.rsauthenticator.ui.theme.faSolid

@Composable
fun PinKeyboard(onFillUp: (pin: String) -> Unit) {

    var pin by remember { mutableStateOf("") }
    val maxPinLength = 4


    RsColumn(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RsRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(maxPinLength) { index ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            1.dp,
                            if (index < pin.length) AppColors.Primary10 else Color.Gray,
                            RoundedCornerShape(12.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    if (index < pin.length) {
                        CustomText(
                            icon = "\uf621",
                            color = AppColors.Primary40,
                            fontFamily = faSolid,
                            fs = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomText(
            text = "Enter PIN", textAlign = TextAlign.Center, color = AppColors.Dark5, fs = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "clear")
        )

        keys.forEach { row ->
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { key ->

                    Box(modifier = Modifier
                        .size(80.dp)
                        .clickable {
                            when {
                                key == "clear" && pin.isNotEmpty() -> pin = pin.dropLast(1)
                                key != "clear" && pin.length < maxPinLength -> pin += key
                            }

                            if (pin.length == 4) {
                                onFillUp(pin)
                            }
                        }
                        .background(Color.Transparent),
                        contentAlignment = Alignment.Center) {

                        CustomText(
                            icon = if (key == "clear") "\uf55a" else key,
                            fs = 24.sp,
                            color = AppColors.Dark10,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }


    }

}