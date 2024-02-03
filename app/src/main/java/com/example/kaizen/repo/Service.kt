package com.example.kaizen.repo

import com.example.kaizen.repo.dataclasses.ResponseGetSports
import retrofit2.Response
import retrofit2.http.GET

interface Services {

    @GET("sports")
    suspend fun getSports(): Response<List<ResponseGetSports>>
}