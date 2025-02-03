package com.rs.rsauthenticator.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

@Composable
fun FlexBox(
    modifier: Modifier = Modifier,
    horizontalSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    verticalSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val horizontalSpacingPx = horizontalSpacing.roundToPx()
        val verticalSpacingPx = verticalSpacing.roundToPx()

        // Variables to track the position and size
        var rowWidth = 0
        var rowHeight = 0
        var totalHeight = 0
        var maxWidth = 0

        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)
            if (rowWidth + placeable.width > constraints.maxWidth) {
                // Move to the next row
                totalHeight += rowHeight + verticalSpacingPx
                rowWidth = 0
                rowHeight = 0
            }
            rowWidth += placeable.width + horizontalSpacingPx
            rowHeight = maxOf(rowHeight, placeable.height)
            maxWidth = maxOf(maxWidth, rowWidth)
            placeable
        }

        totalHeight += rowHeight // Add the last row height

        layout(width = constraints.maxWidth, height = totalHeight) {
            var xPosition = 0
            var yPosition = 0
            rowWidth = 0
            rowHeight = 0

            placeables.forEach { placeable ->
                if (rowWidth + placeable.width > constraints.maxWidth) {
                    // Move to the next row
                    xPosition = 0
                    yPosition += rowHeight + verticalSpacingPx
                    rowWidth = 0
                    rowHeight = 0
                }
                placeable.placeRelative(x = xPosition, y = yPosition)
                xPosition += placeable.width + horizontalSpacingPx
                rowWidth += placeable.width + horizontalSpacingPx
                rowHeight = maxOf(rowHeight, placeable.height)
            }
        }
    }
}