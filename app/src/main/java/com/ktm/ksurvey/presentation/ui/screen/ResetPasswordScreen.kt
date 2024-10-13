package com.ktm.ksurvey.presentation.ui.screen

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ktm.ksurvey.R
import com.ktm.ksurvey.presentation.ui.common.ACTION_BAR_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.ActionButton
import com.ktm.ksurvey.presentation.ui.common.DefaultButton
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.ui.common.LoadingView
import com.ktm.ksurvey.presentation.ui.common.LoginLogo
import com.ktm.ksurvey.presentation.ui.common.PADDING_HORIZONTAL
import com.ktm.ksurvey.presentation.ui.common.STATUS_BAR_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.VerticalDivider
import com.ktm.ksurvey.presentation.ui.common.showToast
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent70
import com.ktm.ksurvey.presentation.util.NotificationHandler
import com.ktm.ksurvey.presentation.viewmodel.AuthViewModel
import com.ktm.ksurvey.presentation.viewmodel.ResetPasswordUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ResetPasswordScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
) {

    val loadingState = remember { mutableStateOf(false) }

    ResetPasswordScreenContainer(
        loadingState = loadingState,
        authViewModel = authViewModel,
        onBackBtnClicked = onNavigateToLoginScreen
    )

    val context = LocalContext.current

    val notificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }
    val notificationHandler = NotificationHandler(context)
    val notificationTitleCheckEmail = stringResource(R.string.notification_title_check_email)

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(authViewModel, lifecycle) {
        authViewModel.resetPasswordUiState.flowWithLifecycle(lifecycle).collect {
            when (it) {
                ResetPasswordUiState.Default -> {}

                ResetPasswordUiState.Loading -> {
                    loadingState.value = true
                }

                is ResetPasswordUiState.ErrorCode -> {
                    loadingState.value = false
                    showToast(context, "Error happens! (error code: ${it.errorCode})")
                }

                is ResetPasswordUiState.ErrorException -> {
                    loadingState.value = true
                    showToast(context, "Error happens!")
                }

                is ResetPasswordUiState.Success -> {
                    loadingState.value = false
                    if (it.message.isNotBlank()) {
                        val showNotification = notificationPermission?.status?.isGranted ?: true
                        if (showNotification) {
                            notificationHandler.showNotification(
                                title = notificationTitleCheckEmail,
                                content = it.message
                            )
                        } else {
                            showToast(context, it.message)
                        }
                    }
                    onNavigateToLoginScreen()
                }
            }
        }
    }
}

@Composable
fun ResetPasswordScreenContainer(
    loadingState: MutableState<Boolean>,
    authViewModel: AuthViewModel,
    onBackBtnClicked: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val emailState = remember { mutableStateOf("") }

        val keyboardController = LocalSoftwareKeyboardController.current

        FullScreenImage(
            painter = painterResource(id = R.drawable.bg_login)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(STATUS_BAR_HEIGHT + ACTION_BAR_HEIGHT)
                .padding(top = STATUS_BAR_HEIGHT)
        ) {
            ActionButton(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_back),
                onClicked = onBackBtnClicked
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = PADDING_HORIZONTAL)
                .padding(top = STATUS_BAR_HEIGHT + ACTION_BAR_HEIGHT),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val spaceHeight = 16.dp

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(272.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginLogo(modifier = Modifier.wrapContentSize())

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(R.string.label_reset_desc),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = ColorWhiteTransparent70
                    ),
                    textAlign = TextAlign.Center
                )
            }
            EditTextEmail(emailState)
            VerticalDivider(spaceHeight)
            DefaultButton(
                onBtnClicked = {
                    keyboardController?.hide()
                    authViewModel.resetPassword(
                        email = emailState.value,
                    )
                },
                text = stringResource(R.string.label_reset),
                enabled = emailState.value.isNotBlank()
            )
        }

        if (loadingState.value) {
            LoadingView(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}