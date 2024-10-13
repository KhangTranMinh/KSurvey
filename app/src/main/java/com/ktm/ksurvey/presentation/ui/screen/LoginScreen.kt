package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.ktm.ksurvey.R
import com.ktm.ksurvey.presentation.ui.common.ACTION_BAR_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.BUTTON_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.CORNER_RADIUS
import com.ktm.ksurvey.presentation.ui.common.DefaultButton
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.ui.common.LoadingView
import com.ktm.ksurvey.presentation.ui.common.LoginLogo
import com.ktm.ksurvey.presentation.ui.common.PADDING_HORIZONTAL
import com.ktm.ksurvey.presentation.ui.common.STATUS_BAR_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.VerticalDivider
import com.ktm.ksurvey.presentation.ui.common.showToast
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent15
import com.ktm.ksurvey.presentation.ui.theme.inputTextStyle
import com.ktm.ksurvey.presentation.ui.theme.placeholderTextStyle
import com.ktm.ksurvey.presentation.viewmodel.AuthViewModel
import com.ktm.ksurvey.presentation.viewmodel.LoginUiState

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToResetPasswordScreen: () -> Unit,
) {
    val loadingState = remember { mutableStateOf(false) }

    LoginScreenContainer(
        loadingState = loadingState,
        authViewModel = authViewModel,
        onForgotClick = onNavigateToResetPasswordScreen
    )

    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(authViewModel, lifecycle) {
        authViewModel.loginUiState.flowWithLifecycle(lifecycle).collect {
            when (it) {
                LoginUiState.Default -> {}

                LoginUiState.Loading -> {
                    loadingState.value = true
                }

                is LoginUiState.ErrorCode -> {
                    loadingState.value = false
                    showToast(context, "Error happens! (error code: ${it.errorCode})")
                }

                is LoginUiState.ErrorException -> {
                    loadingState.value = true
                    showToast(context, "Error happens!!!")
                }

                LoginUiState.Success -> {
                    loadingState.value = false
                    onNavigateToHomeScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreenContainer(
    loadingState: MutableState<Boolean>,
    authViewModel: AuthViewModel,
    onForgotClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }

        val keyboardController = LocalSoftwareKeyboardController.current

        FullScreenImage(
            painter = painterResource(id = R.drawable.bg_login)
        )

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = PADDING_HORIZONTAL)
                .padding(top = STATUS_BAR_HEIGHT + ACTION_BAR_HEIGHT),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val spaceHeight = 16.dp

            LoginLogo(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(272.dp)
            )
            EditTextEmail(emailState)
            VerticalDivider(spaceHeight)
            EditTextPassword(
                passwordState = passwordState,
                onForgotClick = {
                    keyboardController?.hide()
                    onForgotClick()
                }
            )
            VerticalDivider(spaceHeight)
            DefaultButton(
                onBtnClicked = {
                    keyboardController?.hide()
                    authViewModel.login(
                        email = emailState.value,
                        password = passwordState.value
                    )
                },
                text = stringResource(R.string.label_login),
                enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank()
            )
        }

        if (loadingState.value) {
            LoadingView(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun EditText(
    textState: MutableState<String>,
    modifier: Modifier,
    placeholder: String,
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType
) {
    TextField(
        value = textState.value,
        onValueChange = { newInputText -> textState.value = newInputText },
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
fun EditTextEmail(
    emailState: MutableState<String>
) {
    EditTextBox(BUTTON_HEIGHT, CORNER_RADIUS) {
        EditText(
            textState = emailState,
            modifier = Modifier
                .fillMaxSize(),
            placeholder = stringResource(R.string.label_email),
            keyboardType = KeyboardType.Email,
            visualTransformation = VisualTransformation.None
        )
    }
}

@Composable
fun EditTextPassword(
    passwordState: MutableState<String>,
    onForgotClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    EditTextBox(BUTTON_HEIGHT, CORNER_RADIUS) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            EditText(
                textState = passwordState,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                placeholder = stringResource(R.string.label_password),
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = stringResource(R.string.label_forgot),
                style = inputTextStyle,
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onForgotClick
                )
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
        }
    }
}