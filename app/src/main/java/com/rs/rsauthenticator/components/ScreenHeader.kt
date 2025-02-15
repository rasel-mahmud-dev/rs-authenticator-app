package com.rs.rsauthenticator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    gapX: Dp? = null,
    h: Dp? = null,
    w: Dp? = null,
    navigate: () -> Unit,
    title: String

) {

    val buttonModifier = modifier
        .then(if (w != null) Modifier.width(w) else Modifier)
        .then(if (h != null) Modifier.height(h) else Modifier)


    Row(
        modifier = buttonModifier
            .fillMaxWidth()
            .zIndex(90F),
//            .background(
//                color = bgColor ?: Color(0xFFF44336), shape = RoundedCornerShape(radius ?: 16.dp)
//            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(gapX ?: 2.dp)
    ) {

        RsIconButton(
            onClick = {
                navigate()
            },
            modifier = Modifier.clip(RoundedCornerShape(15.dp)),
            h = 40.dp,
            w = 40.dp,
        ) {
            CustomText(icon = "\uf053", fs = 16.sp)
        }

        CustomText(text = title, fontWeight = FontWeight.Medium, fs = 20.sp)
    }
}