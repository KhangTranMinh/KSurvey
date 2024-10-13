package com.ktm.ksurvey.presentation.ui.common

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ktm.ksurvey.R
import com.ktm.ksurvey.presentation.ui.theme.ColorBlackTransparent50
import com.ktm.ksurvey.presentation.ui.theme.ColorWhite
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent50
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent70
import com.ktm.ksurvey.presentation.ui.theme.btnTextStyle

val CORNER_RADIUS = 32.dp
val BUTTON_HEIGHT = 54.dp
val PADDING_HORIZONTAL = 28.dp
val MENU_ITEM_HEIGHT = 56.dp
val MENU_PADDING_HORIZONTAL = 16.dp
val STATUS_BAR_HEIGHT = 24.dp
val ACTION_BAR_HEIGHT = 48.dp

val roundedCornerShape = RoundedCornerShape(CORNER_RADIUS)

val showToast: (context: Context, message: String) -> Unit =
    { context, message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

@Composable
fun FullScreenImage(
    modifier: Modifier = Modifier,
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onBtnClicked: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {

    val currentOnBtnClicked = remember { onBtnClicked }

    Button(
        onClick = currentOnBtnClicked,
        modifier = modifier
            .fillMaxWidth()
            .height(BUTTON_HEIGHT),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorWhite,
            disabledContainerColor = ColorWhiteTransparent50
        ),
        shape = roundedCornerShape,
        enabled = enabled
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
fun LoadingView(
    modifier: Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
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
            modifier = Modifier.size(48.dp, 48.dp),
            color = ColorWhiteTransparent70
        )
    }
}

@Composable
fun LoginLogo(modifier: Modifier) {
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
fun ActionButton(
    modifier: Modifier,
    painter: Painter,
    onClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(ACTION_BAR_HEIGHT)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClicked
            )
    ) {
        Image(
            modifier = Modifier
                .size(30.dp)
                .align(alignment = Alignment.Center),
            painter = painter,
            contentDescription = null
        )
    }
}