package com.ktm.ksurvey.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ktm.ksurvey.presentation.ui.theme.ColorBlackTransparent50
import com.ktm.ksurvey.presentation.ui.theme.ColorWhite
import com.ktm.ksurvey.presentation.ui.theme.btnTextStyle

val CORNER_RADIUS = 32.dp
val BUTTON_HEIGHT = 54.dp
val PADDING_HORIZONTAL = 28.dp

val roundedCornerShape = RoundedCornerShape(CORNER_RADIUS)

@Composable
fun FullScreenImage(
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun DefaultButton(
    onBtnClicked: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {

    val currentOnBtnClicked = remember { onBtnClicked }

    Button(
        onClick = currentOnBtnClicked,
        modifier = modifier
            .fillMaxWidth()
            .height(BUTTON_HEIGHT),
        colors = ButtonDefaults.buttonColors(containerColor = ColorWhite),
        shape = roundedCornerShape
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = btnTextStyle
        )
    }
}

@Composable
fun VerticalDivider(
    height: Dp,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier.size(height)
    )
}

@Composable
fun LoadingView() {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = ColorBlackTransparent50
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp, 48.dp)
        )
    }
}