package com.example.kaizen.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.kaizen.R
import com.example.kaizen.repo.dataclasses.MainScreenDataClass
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import com.example.kaizen.repo.dataclasses.MatchDetails
import com.example.kaizen.repo.sealedClasses.ViewState

class MainScreenView : BaseViewFactory<MainScreenUiModel>() {
    @Composable
    override fun HandleState(viewState: ViewState<MainScreenUiModel>) {
        when (viewState) {
            is ViewState.Error -> {
                ErrorComposable()
            }

            is ViewState.Loading -> {
                LoadingComposable()
            }

            is ViewState.Loaded<MainScreenUiModel> -> {
                MainScreen(viewState.result)
            }
        }
    }

}

@Composable
fun MainScreen(uiModel: MainScreenUiModel) {
    HeaderView()
    LazyColumn(
        modifier = Modifier
            .wrapContentWidth()
            .background(colorResource(id = R.color.greyKaizen))
    ) {
        items(uiModel.model.value.size){
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
            EachSportView(uiModel.model.value[it])
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EachSportView(data: MainScreenDataClass) {
    Column(modifier = Modifier.wrapContentWidth()) {
        val isBodyExpanded by remember { mutableStateOf(true) }
        SportsViewHeader(data)
        if (isBodyExpanded) {
            FlowRow{
//                items( data.matchDetails?.size ?: return@LazyRow){
//                    SportsViewBody(data.matchDetails.get(it) ?: return@items)
//                }
                repeat(data.matchDetails?.size ?: return@FlowRow){
                    SportsViewBody(data.matchDetails[it])
                }
            }
        }
    }
}

@Composable
private fun SportsViewHeader(data: MainScreenDataClass) {
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
            isFavorite = data.isFavorite,
            modifier = Modifier.constrainAs(favorite) {
                end.linkTo(arrow.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Image(painter = painterResource(id = R.drawable.ic_arrow_up),
            contentDescription = "",
            modifier = Modifier
                .constrainAs(arrow) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(40.dp)
                .padding(horizontal = 8.dp))
    }
}

@Composable
private fun SportsViewBody(data : MatchDetails) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var checked by remember {
            mutableStateOf(data.isGameFavorite)
        }

        Text(text = data.timeRemaining.toString(), color = Color.White)

        IconToggleButton(
            checked = checked,
            onCheckedChange = { isChecked ->
                checked = isChecked
            }
        ) {
            Icon(
                modifier = Modifier.border(
                    width = if (checked) 1.dp else 0.dp,
                    color = Color.White
                ),
                imageVector = Icons.Default.Star,
                contentDescription = "",
                tint = if (checked) colorResource(id = R.color.yellowKaizen) else colorResource(id = R.color.yellowKaizen)
            )
        }

        Text(text = data.competitors?.first ?: "", color = Color.White)

        Text(text = "VS", color = colorResource(id = R.color.redKaizen))

        Text(text = data.competitors?.second ?: "", color = Color.White)
    }
}

@Composable
fun SwitchWithCustomIcon(isFavorite: Boolean, modifier: Modifier) {
    var checked by remember { mutableStateOf(isFavorite) }

    Switch(
        modifier = modifier.wrapContentSize(),
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = if (checked) colorResource(id = R.color.blueKaizen) else colorResource(
                id = R.color.greyKaizen
            ),
            checkedTrackColor = colorResource(id = R.color.greyKaizen),
            uncheckedThumbColor = Color.LightGray,
            uncheckedTrackColor = Color.LightGray,
        ),
        thumbContent = if (checked) {
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


//@Preview
//@Composable
//fun SwitchPreview() {
//    SwitchWithCustomIcon(modifier = Modifier)
//}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun SwitchPreview2() {
Column {
    FlowRow(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Black)) {

        SportsViewBody(MatchDetails(
            timeRemaining = 5000,
            isGameFavorite = false,
            competitors = Pair("homeeeee","awayyyyyyyyyy")
        ))
        SportsViewBody(
            MatchDetails(
                timeRemaining = 5000,
                isGameFavorite = false,
                competitors = Pair("homeeeee","awayyyyyyyyyy")
            )
        )
        SportsViewBody(
            MatchDetails(
                timeRemaining = 5000,
                isGameFavorite = false,
                competitors = Pair("homeeeee","awayyyyyyyyyy")
            )
        )
        SportsViewBody(
            MatchDetails(
                timeRemaining = 5000,
                isGameFavorite = false,
                competitors = Pair("homeeeee","awayyyyyyyyyy")
            )
        )
        SportsViewBody(
            MatchDetails(
                timeRemaining = 5000,
                isGameFavorite = false,
                competitors = Pair("homeeeee","awayyyyyyyyyy")
            )
        )
    }
}}


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