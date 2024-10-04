package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ktm.ksurvey.R
import com.ktm.ksurvey.presentation.ui.common.BUTTON_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.CORNER_RADIUS
import com.ktm.ksurvey.presentation.ui.common.DefaultButton
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.ui.common.PADDING_HORIZONTAL
import com.ktm.ksurvey.presentation.ui.common.VerticalDivider
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent15
import com.ktm.ksurvey.presentation.ui.theme.inputTextStyle
import com.ktm.ksurvey.presentation.ui.theme.placeholderTextStyle

@Composable
fun LoginScreen(
    onNavigateToHomeScreen: () -> Unit,
) {
    LoginScreenContainer(onNavigateToHomeScreen)
}

@Composable
fun LoginScreenContainer(
    onNavigateToHomeScreen: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FullScreenImage(
            painter = painterResource(id = R.drawable.bg_login)
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = PADDING_HORIZONTAL)
                .align(alignment = Alignment.Center)
        ) {
            val spaceHeight = 16.dp
            val topAndBottomSpaceModifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1F)

            ImageLogo(topAndBottomSpaceModifier)
            EditTextEmail()
            VerticalDivider(spaceHeight)
            EditTextPassword()
            VerticalDivider(spaceHeight)
            DefaultButton(
                onBtnClicked = onNavigateToHomeScreen,
                text = stringResource(R.string.label_login)
            )
            Spacer(topAndBottomSpaceModifier)
        }
    }
}

@Composable
fun ImageLogo(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_small),
            contentDescription = null,
            modifier = Modifier
                .align(alignment = Alignment.Center)
        )
    }
}

@Composable
fun EditText(
    modifier: Modifier,
    placeholder: String,
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType
) {
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = inputText,
        onValueChange = { newInputText -> inputText = newInputText },
        modifier = modifier,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                style = placeholderTextStyle
            )
        },
        visualTransformation = visualTransformation,
        textStyle = inputTextStyle,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun EditTextBox(
    height: Dp,
    cornerRadius: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                color = ColorWhiteTransparent15,
                shape = RoundedCornerShape(cornerRadius)
            ),
        content = content
    )
}

@Composable
fun EditTextEmail() {
    EditTextBox(BUTTON_HEIGHT, CORNER_RADIUS) {
        EditText(
            modifier = Modifier
                .fillMaxSize(),
            placeholder = stringResource(R.string.label_email),
            keyboardType = KeyboardType.Email,
            visualTransformation = VisualTransformation.None
        )
    }
}

@Composable
fun EditTextPassword() {
    EditTextBox(BUTTON_HEIGHT, CORNER_RADIUS) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            EditText(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                placeholder = stringResource(R.string.label_password),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = stringResource(R.string.label_forgot),
                style = inputTextStyle
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenContainer { }
}