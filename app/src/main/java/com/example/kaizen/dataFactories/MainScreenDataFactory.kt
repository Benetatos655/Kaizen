package com.example.kaizen.dataFactories

import com.example.kaizen.R
import com.example.kaizen.repo.dataclasses.MainScreenDataClass
import com.example.kaizen.repo.dataclasses.MatchDetails
import com.example.kaizen.repo.dataclasses.ResponseGetSports
import com.example.kaizen.repo.dataclasses.ResponseSportInfo
import javax.inject.Inject

class MainScreenDataFactory @Inject constructor() {

    fun transformData(body: List<ResponseGetSports>?) : List<MainScreenDataClass>? {
        // if i also had to create the pre live view or one other view similar to the live i would have use interface with the vals and the data class
        // would have extend interface but only for one i think it's extra unnecessary implementation
        return body?.map { sport ->
            MainScreenDataClass(
                sportsIcon = fetchSportIcon(sport.i ?: ""),
                sportsText = sport.d ?: "",
                isFavorite = false,
                matchDetails = transformDataToMatchDetails(sport.e)
            )
        }
    }

    private fun transformDataToMatchDetails(data: List<ResponseSportInfo?>): List<MatchDetails> {
        return data.map {
            MatchDetails(
                timeRemaining = it?.tt ?: 0,
                competitors = it?.returnHomeCompetitor(),
                isGameFavorite = false,
                id = it?.i
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