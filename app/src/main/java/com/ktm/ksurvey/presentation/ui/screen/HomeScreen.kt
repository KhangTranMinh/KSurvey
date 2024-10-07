package com.ktm.ksurvey.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ktm.ksurvey.R
import com.ktm.ksurvey.domain.entity.Survey
import com.ktm.ksurvey.domain.entity.User
import com.ktm.ksurvey.domain.repository.result.exception.NoUserFoundException
import com.ktm.ksurvey.presentation.ui.common.BUTTON_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.DefaultButton
import com.ktm.ksurvey.presentation.ui.common.FullScreenImage
import com.ktm.ksurvey.presentation.ui.common.LoadingView
import com.ktm.ksurvey.presentation.ui.common.MENU_ITEM_HEIGHT
import com.ktm.ksurvey.presentation.ui.common.MENU_PADDING_HORIZONTAL
import com.ktm.ksurvey.presentation.ui.common.PADDING_HORIZONTAL
import com.ktm.ksurvey.presentation.ui.common.VerticalDivider
import com.ktm.ksurvey.presentation.ui.common.showToast
import com.ktm.ksurvey.presentation.ui.theme.ColorDark
import com.ktm.ksurvey.presentation.ui.theme.ColorMenu
import com.ktm.ksurvey.presentation.ui.theme.ColorWhite
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent15
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent20
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent30
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent50
import com.ktm.ksurvey.presentation.ui.theme.ColorWhiteTransparent70
import com.ktm.ksurvey.presentation.util.DateTimeUtil
import com.ktm.ksurvey.presentation.viewmodel.HomeUiState
import com.ktm.ksurvey.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToThankYouScreen: () -> Unit,
    onNavigateLogout: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    // loading view
    val loadingState = remember { mutableStateOf(false) }

    // left menu
    val navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val user by homeViewModel.userState.collectAsState()

    // use for pager
    val lazyPagingItems = homeViewModel.surveyState.collectAsLazyPagingItems()
    val horizontalPagerState = PagerState(pageCount = { lazyPagingItems.itemCount })

    // pull to refresh
    val pullRefreshIndicatorState = remember { mutableStateOf(false) }

    HomeScreenContainer(
        coroutineScope = coroutineScope,
        loadingState = loadingState,
        navigationDrawerState = navigationDrawerState,
        pullRefreshIndicatorState = pullRefreshIndicatorState,
        user = user,
        lazyPagingItems = lazyPagingItems,
        horizontalPagerState = horizontalPagerState,
        onLogoutClicked = { homeViewModel.logout() },
        onBtnTakeSurveyClicked = onNavigateToThankYouScreen,
    )

    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(homeViewModel, lifecycle) {
        homeViewModel.getUser()
        homeViewModel.fetchSurveys()

        homeViewModel.homeUiState.flowWithLifecycle(lifecycle).collect {
            when (it) {
                HomeUiState.Default -> {}

                HomeUiState.Loading -> {
                    loadingState.value = true
                }

                HomeUiState.Logout -> {
                    loadingState.value = false
                    onNavigateLogout()
                }

                is HomeUiState.ErrorCode -> {
                    loadingState.value = false
                    showToast(context, "Error happens! (error code: ${it.errorCode}")
                }

                is HomeUiState.ErrorException -> {
                    loadingState.value = true
                    if (it.throwable is NoUserFoundException) {
                        showToast(context, "Error happens! No user found")
                        homeViewModel.clearAllData()
                    } else {
                        showToast(context, "Error happens!")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContainer(
    coroutineScope: CoroutineScope,
    loadingState: MutableState<Boolean>,
    navigationDrawerState: DrawerState,
    pullRefreshIndicatorState: MutableState<Boolean>,
    user: User,
    lazyPagingItems: LazyPagingItems<Survey>,
    horizontalPagerState: PagerState,
    onLogoutClicked: () -> Unit,
    onBtnTakeSurveyClicked: () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = pullRefreshIndicatorState.value,
        refreshThreshold = 120.dp,
        onRefresh = {
            coroutineScope.launch {
                pullRefreshIndicatorState.value = true
                delay(1000L)
                lazyPagingItems.refresh()
                pullRefreshIndicatorState.value = false
                horizontalPagerState.animateScrollToPage(0)
            }
        }
    )

    ModalNavigationDrawer(
        modifier = Modifier.background(color = ColorDark),
        drawerState = navigationDrawerState,
        drawerContent = {
            // left menu
            NavigationDrawerContent(
                user = user,
                onLogoutClicked = onLogoutClicked
            )
        },
    ) {
        BoxWithConstraints {
            val realMaxHeight = this.maxHeight + 0.1.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(realMaxHeight)
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState()),
            ) {
                HorizontalPager(
                    state = horizontalPagerState
                ) { page ->
                    lazyPagingItems[page]?.let { survey ->
                        PagerItem(
                            survey = survey,
                            modifier = Modifier
                                .fillMaxSize()
                                .height(realMaxHeight)
                        )
                    }
                }

                // indicator as bullets
                HorizontalIndicator(
                    horizontalPagerState = horizontalPagerState,
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(horizontal = PADDING_HORIZONTAL)
                        .padding(bottom = HomeScreenConst.SECTION_INDICATOR_PADDING_BOTTOM)
                )

                // top card with avatar
                ProfileCard(
                    user = user,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onAvatarClick = {
                        coroutineScope.launch {
                            if (navigationDrawerState.isClosed) navigationDrawerState.open()
                        }
                    },
                )

                // button Take This Survey
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HomeScreenConst.SECTION_BUTTON_HEIGHT)
                        .padding(horizontal = PADDING_HORIZONTAL)
                        .align(alignment = Alignment.BottomCenter)
                ) {
                    DefaultButton(
                        onBtnClicked = onBtnTakeSurveyClicked,
                        text = stringResource(R.string.label_take_this_survey)
                    )
                }

                // pull to refresh indicator
                PullRefreshIndicator(
                    refreshing = pullRefreshIndicatorState.value,
                    state = pullRefreshState,
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                )
            }
        }
    }

    // loading view
    if (loadingState.value) {
        LoadingView(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun NavigationDrawerContent(
    user: User,
    onLogoutClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    ModalDrawerSheet(
        drawerContainerColor = ColorMenu,
        drawerShape = RectangleShape
    ) {
        Box(
            modifier = Modifier
                .width(256.dp)
                .fillMaxHeight()
                .padding(horizontal = MENU_PADDING_HORIZONTAL)
                .padding(top = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .padding(
                            vertical = MENU_PADDING_HORIZONTAL,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.name,
                        style = TextStyle(
                            fontWeight = FontWeight(850),
                            fontSize = 28.sp,
                            color = ColorWhite
                        ),
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(end = 8.dp)
                            .weight(1F),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.avatarUrl)
                            .build(),
                        placeholder = ColorPainter(ColorDark),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(shape = CircleShape)
                    )
                }
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = ColorWhiteTransparent20
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MENU_ITEM_HEIGHT)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onLogoutClicked
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart),
                        text = stringResource(R.string.label_logout),
                        style = TextStyle(
                            color = ColorWhiteTransparent50,
                            fontWeight = FontWeight.W400,
                            fontSize = 20.sp
                        ),
                    )
                }
            }

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .align(alignment = Alignment.BottomStart),
                text = "v1.0.0",
                style = TextStyle(
                    color = ColorWhiteTransparent50,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
fun ProfileCard(
    user: User,
    modifier: Modifier,
    onAvatarClick: () -> Unit,
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
                        text = DateTimeUtil.getFormatedDate(),
                        style = TextStyle(
                            fontWeight = FontWeight(850),
                            fontSize = 14.sp,
                            color = ColorWhite
                        )
                    )
                    ProfileText(
                        text = stringResource(R.string.label_today),
                        style = TextStyle(
                            fontWeight = FontWeight.W800,
                            fontSize = 34.sp,
                            color = ColorWhite
                        )
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.avatarUrl)
                        .build(),
                    placeholder = ColorPainter(ColorWhiteTransparent15),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(shape = CircleShape)
                        .clickable { onAvatarClick() }
                )
            }
        }
    }
}

@Composable
fun HorizontalIndicator(
    horizontalPagerState: PagerState,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(horizontalPagerState.pageCount) { iteration ->
            val backgroundColor = if (horizontalPagerState.currentPage == iteration)
                ColorWhite
            else
                ColorWhiteTransparent30

            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color = backgroundColor)
                    .size(6.dp)
            )
        }
    }
}

@Composable
fun PagerItem(
    survey: Survey,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
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
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(survey.coverImageUrl)
                .build(),
            placeholder = ColorPainter(ColorDark),
            error = ColorPainter(ColorDark),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        FullScreenImage(
            modifier = Modifier.fillMaxSize(),
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
            .padding(bottom = HomeScreenConst.SECTION_CONTENT_PADDING_BOTTOM)
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
            text = survey.description,
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
        minLines = 1,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
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
    private val BOTTOM_INSET = 48.dp
    val SECTION_BUTTON_HEIGHT = BUTTON_HEIGHT + BOTTOM_INSET
    val SECTION_CONTENT_PADDING_BOTTOM = SECTION_BUTTON_HEIGHT + 24.dp
    val SECTION_INDICATOR_PADDING_BOTTOM = SECTION_BUTTON_HEIGHT + 156.dp
}