package test.citron.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import test.citron.design.theme.CitronTheme

private const val EMPTY_STRING = ""

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    currentText: String,
    placeHolderText: String = "",
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    paddingValues: PaddingValues,
    enabled: Boolean = true,
    clearFocusAutomatically: Boolean = true,
    onClearAction: ((String) -> Unit)? = null,
    onFocusAction: () -> Unit,
    textStyle: TextStyle?
) {
    if (clearFocusAutomatically) {
        val focusManager = LocalFocusManager.current
        val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

        LaunchedEffect(imeVisible) {
            if (imeVisible.not()) {
                focusManager.clearFocus(force = true)
            }
        }
    }

    var isFocused by remember { mutableStateOf(false) }

    TextField(
        value = currentText,
        enabled = enabled,
        singleLine = true,
        onValueChange = {
            onValueChange.invoke(it)
        },
        placeholder = {
            if (currentText.isEmpty()) {
                Text(
                    text = placeHolderText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier =
        modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) onFocusAction()
            },
        shape = RoundedCornerShape(CitronTheme.sizing.roundedCornerMedium),
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (isFocused && currentText.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onClearAction?.invoke(EMPTY_STRING)
                        onValueChange.invoke(EMPTY_STRING)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Black,
                        modifier =
                        Modifier
                            .background(Color.White, shape = CircleShape)
                            .size(24.dp)
                    )
                }
            } else {
                trailingIcon?.invoke()
            }
        },
        colors =
        TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        textStyle = textStyle ?: TextStyle.Default
    )
}

@Preview(showBackground = true)
@Composable
fun SearchTextFieldPreview() {
    SearchTextField(
        currentText = "",
        placeHolderText = "Testing",
        onValueChange = {},
        leadingIcon = null,
        trailingIcon = null,
        paddingValues = PaddingValues(16.dp),
        enabled = true,
        onFocusAction = {},
        textStyle = TextStyle(fontSize = 16.sp)
    )
}
