package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ktm.ksurvey.presentation.viewmodel.MainViewModel
import com.ktm.ksurvey.R
import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.presentation.ui.common.BUTTON_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.DefaultButton
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.ui.common.PADDING_HORIZONTAL
import com.ktm.ksurvey.presentation.ui.common.VerticalDivider
import com.ktm.ksurvey.presentation.ui.theme.ColorDark
import com.ktm.ksurvey.presentation.ui.theme.ColorWhite
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent15
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent70

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigateToThankYouScreen: () -> Unit,
) {
    LaunchedEffect(true) {
        mainViewModel.fetchSurveys()
    }

    HomeScreenContainer(
        mainViewModel = mainViewModel,
        onBtnClicked = onNavigateToThankYouScreen
    )
}

@Composable
fun HomeScreenContainer(
    mainViewModel: MainViewModel,
    onBtnClicked: () -> Unit,
) {
    val pagingItems = mainViewModel.surveyState.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = PagerState(
                pageCount = { pagingItems.itemCount }
            )
        ) { page ->
            pagingItems[page]?.let { survey ->
                PagerItem(survey)
            }
        }

        ProfileCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(HomeScreenConst.BOTTOM_BOX_HEIGHT)
                .padding(horizontal = PADDING_HORIZONTAL)
                .align(alignment = Alignment.BottomCenter)
        ) {
            DefaultButton(
                onBtnClicked = onBtnClicked,
                text = stringResource(R.string.label_take_this_survey)
            )
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(all = PADDING_HORIZONTAL)
            .padding(top = PADDING_HORIZONTAL)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(96.dp),
            shape = RoundedCornerShape(20),
            colors = CardDefaults.cardColors(
                containerColor = ColorWhiteTransparent15,
                disabledContainerColor = ColorWhiteTransparent15,
                contentColor = ColorWhiteTransparent15,
                disabledContentColor = ColorWhiteTransparent15,
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .wrapContentHeight()
                ) {
                    ProfileText(
                        text = "Monday, JUNE 15",
                        style = TextStyle(
                            fontWeight = FontWeight(850),
                            fontSize = 14.sp,
                            color = ColorWhite
                        )
                    )
                    ProfileText(
                        text = "Today",
                        style = TextStyle(
                            fontWeight = FontWeight.W800,
                            fontSize = 34.sp,
                            color = ColorWhite
                        )
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .build(),
                    placeholder = ColorPainter(ColorWhiteTransparent15),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(shape = CircleShape)
                )
            }
        }
    }
}

@Composable
fun PagerItem(
    survey: Survey
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        SurveyImage(
            survey = survey,
        )
        SurveyContent(
            survey = survey,
            modifier = Modifier.align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
fun SurveyImage(
    survey: Survey,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(survey.coverImageUrl)
                .build(),
            placeholder = ColorPainter(ColorDark),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
        FullScreenImage(
            painter = painterResource(R.drawable.bg_overlay)
        )
    }
}

@Composable
fun SurveyContent(
    survey: Survey,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = PADDING_HORIZONTAL)
            .padding(bottom = HomeScreenConst.BOTTOM_BOX_HEIGHT + 24.dp)
    ) {
        SurveyText(
            text = survey.title,
            style = TextStyle(
                color = ColorWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight(850)
            )
        )
        VerticalDivider(
            height = 28.dp
        )
        SurveyText(
            text = survey.desc,
            style = TextStyle(
                color = ColorWhiteTransparent70,
                fontSize = 16.sp,
                fontWeight = FontWeight.W400
            )
        )
    }
}

@Composable
fun SurveyText(
    text: String,
    style: TextStyle
) {
    Text(
        text = text,
        style = style,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = PADDING_HORIZONTAL),
        textAlign = TextAlign.Center,
        maxLines = 2
    )
}

@Composable
fun ProfileText(
    text: String,
    style: TextStyle
) {
    Text(
        text = text,
        style = style,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        maxLines = 1
    )
}

object HomeScreenConst {
    val BOTTOM_BOX_HEIGHT = BUTTON_HEIGHT + 48.dp
}