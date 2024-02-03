package com.example.kaizen.repo

import javax.inject.Inject

class Repository @Inject
constructor(private val service: Services) {
    suspend fun getSports() = service.getSports()
}