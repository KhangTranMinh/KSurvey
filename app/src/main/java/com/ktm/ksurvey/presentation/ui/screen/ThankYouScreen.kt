package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ktm.ksurvey.R
import com.ktm.ksurvey.presentation.ui.theme.ColorDark
import com.ktm.ksurvey.presentation.ui.theme.ColorWhite
import kotlinx.coroutines.delay

@Composable
fun ThankYouScreen(
    onNavigateToHomeScreen: () -> Unit
) {

    LaunchedEffect(true) {
        delay(5000L)
        onNavigateToHomeScreen()
    }

    ThankYouScreenContainer()
}

@Composable
fun ThankYouScreenContainer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = ColorDark)
    ) {
        TopSpace()
        CongratulationAnimation()
        CongratulationText()
    }
}

@Composable
fun TopSpace() {
    val size = LocalConfiguration.current.screenHeightDp.dp * 1 / 4
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(size)
    )
}

@Composable
fun CongratulationText() {
    Text(
        text = stringResource(R.string.label_thank_you),
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = ColorWhite,
            fontWeight = FontWeight.W800,
            fontSize = 28.sp
        ),
    )
}

@Composable
fun CongratulationAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_congratulation)
    )
    val size = LocalConfiguration.current.screenWidthDp.dp * 2 / 3
    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .width(size)
            .height(size),
        iterations = LottieConstants.IterateForever
    )
}