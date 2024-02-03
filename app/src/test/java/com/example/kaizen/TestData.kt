package com.example.kaizen

import com.example.kaizen.repo.dataclasses.ResponseGetSports
import com.google.gson.Gson

open class TestData {

    val dummyData : Array<ResponseGetSports>
        get() {
            val jsonString = javaClass.classLoader.getResourceAsStream("getSports.json").readStreamGetString()
            return Gson().fromJson(jsonString,Array<ResponseGetSports>::class.java)
        }
}