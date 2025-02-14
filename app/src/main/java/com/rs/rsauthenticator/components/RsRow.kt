package com.rs.rsauthenticator.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun RsRow(
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
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    spacing: Dp = 0.dp, // Extra param for spacing
    content: @Composable RowScope.() -> Unit
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

    Row(
        modifier = finalModifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        content()
    }
}
