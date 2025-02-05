package com.rs.rsauthenticator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RsBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onClose: () -> Unit,
    handlerColor: Color = Color.Gray, // Handler color
    backgroundColor: Color = BottomSheetDefaults.ContainerColor, // Default background color
    contentColor: Color = contentColorFor(backgroundColor),
    cornerRadius: Dp = 24.dp, // Corner radius for shape
    dragHandleHeight: Dp = 6.dp, // Thickness of drag handle
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onClose() },
        sheetState = sheetState,
        containerColor = backgroundColor,
        contentColor = Color.Red,
        shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius) // Custom shape
    ) {
        RsRow(
            modifier = Modifier
                .background(handlerColor)
                .height(dragHandleHeight)
        ) {}

        content()
    }
}
