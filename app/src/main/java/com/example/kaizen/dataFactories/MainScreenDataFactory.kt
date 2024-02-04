package com.example.kaizen.dataFactories

import androidx.compose.runtime.mutableStateOf
import com.example.kaizen.R
import com.example.kaizen.repo.LocalStorage
import com.example.kaizen.repo.dataclasses.MainScreenDataClass
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import com.example.kaizen.repo.dataclasses.MatchDetails
import com.example.kaizen.repo.dataclasses.ResponseGetSports
import com.example.kaizen.repo.dataclasses.ResponseSportInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainScreenDataFactory @Inject constructor() {

    suspend fun transformData(body: List<ResponseGetSports>?) = withContext(Dispatchers.IO) {
        // if i also had to create the pre live view or one other view similar to the live i would have use interface with the vals and the data class
        // would have extend interface but only for one i think it's extra unnecessary implementation
        var response = body?.map { sport ->
            MainScreenDataClass(
                sportsIcon = fetchSportIcon(sport.sportId ?: ""),
                sportsText = sport.sportName ?: "",
                sportsId = sport.sportId,
                matchDetails = mutableStateOf(transformDataToMatchDetails(sport.activeEvents)),
                isFavorite = mutableStateOf(fetchFavoriteFromBase(sport.sportId ?: ""))
            )
        }
        response = response?.sortedByDescending { it.isFavorite.value }
        return@withContext response
    }

    fun addToFavoriteTheGame(uiModel: MainScreenUiModel, id: Pair<String, String>) {
        val (sportId, gameId) = id

        val sport = uiModel.model.value.firstOrNull { it.sportsId == sportId }
        sport?.matchDetails?.value?.firstOrNull {
            it.id == gameId
        }?.apply {
            isGameFavorite.value = !isGameFavorite.value
        }
        sport?.matchDetails?.value =
            sport?.matchDetails?.value?.sortedByDescending { it.isGameFavorite.value } ?: return

    }

    fun addToFavoriteTheSport(uiModel: MainScreenUiModel, id: String) {

        uiModel.model.value.firstOrNull { it.sportsId == id }?.apply {
            isFavorite.value = !isFavorite.value
        }
        uiModel.model.value = uiModel.model.value.sortedByDescending { it.isFavorite.value }
        //i will use local storage only for the sports , if I add it for games the complexity will go through the roof
        LocalStorage.favoriteSports.setRemoveFavoriteSport(id)
    }

    fun expandCollapseSport(uiModel: MainScreenUiModel, id: String) {

        uiModel.model.value.firstOrNull { it.sportsId == id }?.apply {
            isExpanded.value = !isExpanded.value
        }
    }

    private fun fetchFavoriteFromBase(sportsId: String): Boolean =
        LocalStorage.favoriteSports.getFavoriteSport()?.contains(sportsId) ?: false

    private suspend fun transformDataToMatchDetails(data: List<ResponseSportInfo?>) =
        withContext(Dispatchers.IO) {
            return@withContext data.map {
                MatchDetails(
                    timeRemaining = mutableStateOf(it?.eventStartTime ?: 0),
                    competitors = it?.returnHomeCompetitor(),
                    id = it?.eventId
                )
            }
        }

    private fun fetchSportIcon(sportsName: String): Int =
        when (sportsName) {
            "FOOT" -> R.drawable.ic_soccer
            "BASK" -> R.drawable.ic_basketball
            "TENN" -> R.drawable.ic_tennis
            "TABL" -> R.drawable.ic_table_tennis
            "VOLL" -> R.drawable.ic_volley
            "ESPS" -> R.drawable.ic_esports
            "ICEH" -> R.drawable.ic_hockey
            "HAND" -> R.drawable.ic_handball
            "SNOO" -> R.drawable.ic_snooker
            "FUTS" -> R.drawable.ic_futsal
            else -> R.drawable.ic_wrong_icon
        }
}