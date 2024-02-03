package com.example.kaizen.repo.sealedClasses

sealed class UserActions {
    data class FavoriteGameClicked(val pair: Pair<String, String>) : UserActions()
    data class FavoriteSportClicked(val id: String) : UserActions()
    data class ExpandCollapseSport(val id: String) : UserActions()
}