package com.example.kaizen.repo.dataclasses


data class MainScreenDataClass (
    @JvmField val sportsIcon: Int? = null,
    @JvmField val sportsText: String? = null,
    @JvmField val isFavorite: Boolean = false,
    @JvmField val matchDetails: List<MatchDetails>? = null
)

data class MatchDetails (
    @JvmField val timeRemaining: Int? = null,
    @JvmField val isGameFavorite: Boolean = false,
    @JvmField val competitors: Pair<String?,String?>? = null,
    @JvmField val id: String? = null
)