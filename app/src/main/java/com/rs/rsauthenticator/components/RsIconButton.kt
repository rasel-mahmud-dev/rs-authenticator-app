package com.rs.rsauthenticator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun RsIconButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    px: Dp? = null,
    pt: Dp? = null,
    pb: Dp? = null,
    pl: Dp? = null,
    pr: Dp? = null,
    py: Dp? = null,
    h: Dp? = null,
    w: Dp? = null,
    radius: Dp? = null,
    bgColor: Color = Color.Transparent,
    content: @Composable (() -> Unit)? = null
) {

    var finalModifier = modifier
        .background(bgColor, shape = RoundedCornerShape(radius ?: 0.dp))

    if (w != null) finalModifier = finalModifier.width(w)
    if (h != null) finalModifier = finalModifier.height(h)

    finalModifier = finalModifier.padding(
        start = pl ?: px ?: 0.dp,
        end = pr ?: px ?: 0.dp,
        top = pt ?: py ?: 0.dp,
        bottom = pb ?: py ?: 0.dp
    )

    if (onClick != null) {
        finalModifier = finalModifier.clickable { onClick() }
    }

//    val buttonModifier = modifier
//        .then(if (w != null) Modifier.width(w) else Modifier)
//        .then(if (h != null) Modifier.height(h) else Modifier)

    Box(
        modifier = finalModifier
            .background(
                color = bgColor ?: Color.Transparent,
                shape = RoundedCornerShape(radius ?: 16.dp)
            )
            .padding(
                start = px ?: 8.dp,
                top = py ?: 8.dp,
                end = px ?: 8.dp,
                bottom = py ?: 8.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        content?.invoke()
    }
}