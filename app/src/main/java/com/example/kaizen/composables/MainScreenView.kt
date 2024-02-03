package com.example.kaizen.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.kaizen.R
import com.example.kaizen.extensions.convertTimeToString
import com.example.kaizen.repo.dataclasses.MainScreenDataClass
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import com.example.kaizen.repo.dataclasses.MatchDetails
import com.example.kaizen.repo.sealedClasses.UserActions
import com.example.kaizen.repo.sealedClasses.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainScreenView(
    private val viewState: ViewState<MainScreenUiModel>,
    private val userActions: (UserActions) -> Unit
) {
    @Composable
    fun HandleState() {
        when (viewState) {
            is ViewState.Error -> {
                ErrorComposable()
            }

            is ViewState.Loading -> {
                LoadingComposable()
            }

            is ViewState.Loaded<MainScreenUiModel> -> {
                MainScreen(uiModel = viewState.result, userActions = userActions)
            }
        }
    }

}

@Composable
fun MainScreen(uiModel: MainScreenUiModel, userActions: (UserActions) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.greyKaizen))
    ) {
        item { HeaderView() }
        items(uiModel.model.value.size) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
            EachSportView(data = uiModel.model.value[it], userActions = userActions)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EachSportView(data: MainScreenDataClass, userActions: (UserActions) -> Unit) {
    Column(modifier = Modifier.wrapContentWidth()) {
        SportsViewHeader(data, userActions)
        AnimatedVisibility(data.isExpanded.value) {
            FlowRow {
                if (data.matchDetails.value.isEmpty()) {
                    NoGamesAvailable()
                } else {
                    repeat(data.matchDetails.value.size) {
                        SportsViewBody(
                            data = data.matchDetails.value[it],
                            userActions = userActions,
                            sportsId = data.sportsId ?: return@FlowRow
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SportsViewHeader(
    data: MainScreenDataClass,
    userActions: (UserActions) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        val (icon, text, favorite, arrow) = createRefs()

        Image(painter = painterResource(id = data.sportsIcon ?: R.drawable.ic_arrow_down),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(40.dp)
                .padding(horizontal = 8.dp))

        Text(
            text = data.sportsText ?: "",
            color = Color.Black,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(icon.end)
                end.linkTo(favorite.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            })

        SwitchWithCustomIcon(
            isFavorite = data.isFavorite.value,
            modifier = Modifier.constrainAs(favorite) {
                end.linkTo(arrow.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            callbackAction = {
                userActions.invoke(
                    UserActions.FavoriteSportClicked(
                        data.sportsId ?: ""
                    )
                )
            }

        )

        Image(painter = painterResource(id = if (data.isExpanded.value) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(arrow) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(40.dp)
                .padding(horizontal = 8.dp)
                .clickable {
                    userActions.invoke(
                        UserActions.ExpandCollapseSport(
                            data.sportsId ?: ""
                        )
                    )
                })
    }
}

@Composable
private fun SportsViewBody(
    data: MatchDetails,
    userActions: (UserActions) -> Unit,
    sportsId: String
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountDownTimerCustom(data)

        IconToggleButton(
            checked = data.isGameFavorite.value,
            onCheckedChange = {
                userActions.invoke(
                    UserActions.FavoriteGameClicked(
                        Pair(
                            first = sportsId,
                            second = data.id ?: ""
                        )
                    )
                )
            }
        ) {
            Icon(
                painter = painterResource(id = if (data.isGameFavorite.value) R.drawable.ic_star_full else R.drawable.ic_star),
                tint = Color.Unspecified,
                contentDescription = "",
            )
        }

        Text(text = data.competitors?.first ?: "", color = Color.White, fontSize = 11.sp)

        Text(text = "VS", color = colorResource(id = R.color.redKaizen))

        Text(text = data.competitors?.second ?: "", color = Color.White, fontSize = 11.sp)
    }
}

@Composable
fun SwitchWithCustomIcon(
    isFavorite: Boolean,
    modifier: Modifier,
    callbackAction: () -> Unit,
) {

    Switch(
        modifier = modifier.wrapContentSize(),
        checked = isFavorite,
        onCheckedChange = {
            callbackAction.invoke()
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = if (isFavorite) colorResource(id = R.color.blueKaizen) else colorResource(
                id = R.color.greyKaizen
            ),
            checkedTrackColor = colorResource(id = R.color.greyKaizen),
            uncheckedThumbColor = Color.LightGray,
            uncheckedTrackColor = Color.LightGray,
        ),
        thumbContent = if (isFavorite) {
            {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    tint = Color.White,
                    contentDescription = "",
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        }
    )
}

@Composable
private fun NoGamesAvailable() {
    Text(
        text = "No matches available",
        fontSize = 20.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun HeaderView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.blueKaizen))
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Kaizen Assigment",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CountDownTimerCustom(data: MatchDetails) {
    rememberCountdownTimerState(
        step = 1000,
        data = data
    )
    Text(
        text = data.timeRemaining.value?.convertTimeToString() ?: "",
        color = Color.White,
        fontSize = 11.sp
    )
}

@Composable
fun rememberCountdownTimerState(
    step: Long = 1000,
    data: MatchDetails
) {
    LaunchedEffect(data.timeRemaining.value, step) {
        while (isActive && (data.timeRemaining.value ?: 0) > 0) {
            delay(step)
            data.timeRemaining.value = (data.timeRemaining.value?.minus(step) ?: 0).coerceAtLeast(0)
        }
    }
}