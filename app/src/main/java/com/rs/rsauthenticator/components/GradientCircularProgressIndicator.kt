package com.rs.rsauthenticator.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun GradientCircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    trackColor: Color = Color(0x00000000)
) {
    Box(
        modifier = modifier
            .size(40.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = size,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF44336),
                    Color(0xFF3151FF)
                ),
                start = Offset.Zero,
                end = Offset(size.width, size.height)
            )

            drawArc(
                brush = gradientBrush,
                startAngle = -90f,
                sweepAngle = 360f * progress,  // Use the progress value for the sweep
                useCenter = false,
                size = size,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}
