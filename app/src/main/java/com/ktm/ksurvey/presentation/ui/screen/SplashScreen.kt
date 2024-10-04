package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.ktm.ksurvey.R
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.viewmodel.AuthViewModel
import com.ktm.ksurvey.presentation.viewmodel.SplashUiState
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToHomeScreen: () -> Unit,
) {
//    val currentNavigateToLoginScreen by rememberUpdatedState(onNavigateToLoginScreen)
//    val currentNavigateToHomeScreen by rememberUpdatedState(onNavigateToHomeScreen)

    SplashScreenContainer()

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(authViewModel, lifecycle) {
        delay(1000L)

        // Whenever the uiState changes
        // call the event when `lifecycle` is at least STARTED
        authViewModel.validateUser()
        authViewModel.splashUiState.flowWithLifecycle(lifecycle).collect {
            when (it) {
                SplashUiState.Default -> {}
                SplashUiState.Home -> onNavigateToHomeScreen()
                SplashUiState.Login -> onNavigateToLoginScreen()

            }
        }

    }
}

@Composable
fun SplashScreenContainer() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FullScreenImage(
            painter = painterResource(id = R.drawable.bg_login)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .align(alignment = Alignment.Center)
        )
    }
}