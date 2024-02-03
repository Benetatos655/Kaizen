package com.example.kaizen.repo.dataclasses

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.kaizen.R


data class MainScreenDataClass(
    @JvmField val sportsIcon: Int? = null,
    @JvmField val sportsText: String? = null,
    @JvmField val sportsId: String? = null,
    @JvmField var isFavorite: MutableState<Boolean> = mutableStateOf(false),
    @JvmField var isExpanded: MutableState<Boolean> = mutableStateOf(true),
    @JvmField val matchDetails: MutableState<List<MatchDetails>> = mutableStateOf(listOf())
)

data class MatchDetails(
    @JvmField var timeRemaining: MutableState<Long?> = mutableStateOf(null),
    @JvmField var isGameFavorite: MutableState<Boolean> = mutableStateOf(false),
    @JvmField val competitors: Pair<String?, String?>? = null,
    @JvmField val id: String? = null
)