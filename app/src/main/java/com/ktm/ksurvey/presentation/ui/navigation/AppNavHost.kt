package com.ktm.ksurvey.presentation.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ktm.ksurvey.presentation.ui.screen.HomeScreen
import com.ktm.ksurvey.presentation.ui.screen.LoginScreen
import com.ktm.ksurvey.presentation.ui.screen.ResetPasswordScreen
import com.ktm.ksurvey.presentation.ui.screen.SplashScreen
import com.ktm.ksurvey.presentation.ui.screen.ThankYouScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = AppScreen.SPLASH.name,
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AppScreen.SPLASH.name) {
            DisableBackButton()
            SplashScreen(
                onNavigateToLoginScreen = {
                    navController.popBackStack()
                    navController.navigate(route = AppScreen.LOGIN.name)
                },
                onNavigateToHomeScreen = {
                    navController.popBackStack()
                    navController.navigate(route = AppScreen.HOME.name)
                }
            )
        }
        composable(route = AppScreen.LOGIN.name) {
            LoginScreen(
                onNavigateToHomeScreen = {
                    navController.popBackStack()
                    navController.navigate(route = AppScreen.HOME.name)
                },
                onNavigateToResetPasswordScreen = {
                    navController.navigate(route = AppScreen.RESET_PASSWORD.name)
                }
            )
        }
        composable(route = AppScreen.RESET_PASSWORD.name) {
            ResetPasswordScreen(
                onNavigateToLoginScreen = {
                    navController.popBackStack()
                    navController.navigate(route = AppScreen.LOGIN.name)
                }
            )
        }
        composable(route = AppScreen.HOME.name) {
            HomeScreen(
                onNavigateToThankYouScreen = {
                    navController.navigate(route = AppScreen.THANK_YOU.name)
                },
                onNavigateLogout = {
                    navController.popBackStack()
                    navController.navigate(route = AppScreen.LOGIN.name)
                }
            )
        }
        composable(route = AppScreen.THANK_YOU.name) {
            DisableBackButton()
            ThankYouScreen(
                onNavigateToHomeScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun DisableBackButton() {
    BackHandler(enabled = true) {
        // do not allow back action for this screen
    }
}