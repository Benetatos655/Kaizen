package com.example.kaizen.repo.dataclasses

interface BaseSportBody {
    val icon: Int
    val sportsTitle: String
    val isFavorite: Boolean
    val baseGameBody: List<BaseGameBody>?
}

interface BaseGameBody {
    val timeRemaining: String
    val isFavoriteGame: Boolean
    val competitorHome: String
    val competitorAway: String
}