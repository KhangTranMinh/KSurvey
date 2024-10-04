package com.ktm.ksurvey.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    Button(
        onClick = onBtnClicked,
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