package com.rs.rsauthenticator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
fun RsColumn(
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
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    spacing: Dp = 0.dp, // Extra param for spacing
    content: @Composable ColumnScope.() -> Unit
) {
    val combinedModifier = modifier
        .then(if (w != null) Modifier.width(w) else Modifier)
        .then(if (h != null) Modifier.height(h) else Modifier)
        .then(
            if (py != null) Modifier.padding(vertical = py)
            else Modifier.padding(top = pt ?: 0.dp, bottom = pb ?: 0.dp)
        )
        .then(
            if (px != null) Modifier.padding(horizontal = px)
            else Modifier.padding(start = pl ?: 0.dp, end = pr ?: 0.dp)
        )
        .background(bgColor, shape = RoundedCornerShape(radius ?: 0.dp))
        .clickable(enabled = onClick != null) { onClick?.invoke() }

    Column(
        modifier = combinedModifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}
