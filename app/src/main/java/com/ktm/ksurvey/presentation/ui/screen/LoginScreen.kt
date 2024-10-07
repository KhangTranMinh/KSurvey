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
import com.ktm.ksurvey.presentation.ui.common.BUTTON_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.CORNER_RADIUS
import com.ktm.ksurvey.presentation.ui.common.DefaultButton
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.ui.common.LoadingView
import com.ktm.ksurvey.presentation.ui.common.PADDING_HORIZONTAL
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
) {
    val loadingState = remember { mutableStateOf(false) }

    LoginScreenContainer(
        loadingState = loadingState,
        authViewModel = authViewModel
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
                    showToast(context, "Error happens!")
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
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val emailState = remember { mutableStateOf("tran.minhkhang.1989@gmail.com") }
        val passwordState = remember { mutableStateOf("12345678") }

        val keyboardController = LocalSoftwareKeyboardController.current

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
            EditTextEmail(emailState)
            VerticalDivider(spaceHeight)
            EditTextPassword(passwordState)
            VerticalDivider(spaceHeight)
            DefaultButton(
                onBtnClicked = {
                    keyboardController?.hide()
                    authViewModel.login(
                        email = emailState.value,
                        password = passwordState.value
                    )
                },
                text = stringResource(R.string.label_login)
            )
            Spacer(topAndBottomSpaceModifier)
        }

        if (loadingState.value) {
            LoadingView(
                modifier = Modifier.fillMaxSize()
            )
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
    passwordState: MutableState<String>
) {
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
                style = inputTextStyle
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
        }
    }
}