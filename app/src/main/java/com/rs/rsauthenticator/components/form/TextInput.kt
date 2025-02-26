package com.rs.rsauthenticator.components.form

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rs.rsauthenticator.ui.theme.Primary40


@Composable
fun TextInput(
    singleLine: Boolean = true,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    radius: Dp = 12.dp,
    placeholder: String,
    label: String?,
    onChange: (TextFieldValue) -> Unit,
    inputTextStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
    placeholderTextStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = Color(0xFFC5837B)
    ),
    labelTextStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = Color(0xFF0A0A0A)
    ),
) {


    var isFocused by remember { mutableStateOf(false) }

    // Animate border color
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Primary40  else Color(0x61B7B7B7),
        animationSpec = tween(durationMillis = 300)
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 1.dp else 1.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Column {
        if (label != null) Text(
            text = label,
            modifier = Modifier
                .padding(2.dp, 8.dp),
            style = labelTextStyle
        )

        Box(
            modifier = modifier
                .padding(0.dp, 0.dp)
                .border(
                    borderWidth,
                    borderColor,
                    shape = RoundedCornerShape(radius)
                )
                .fillMaxWidth(),
        ) {

            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = inputTextStyle.copy(textAlign = TextAlign.Start),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = keyboardType,
                ),
                singleLine = singleLine,
                keyboardActions = KeyboardActions.Default,
                visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 15.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                cursorBrush = SolidColor(Primary40)
            )

            if (value.text.isBlank()) {
                Text(
                    text = placeholder,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(10.dp, 0.dp),
                    style = placeholderTextStyle
                )
            }
        }
    }
}
