package com.example.kaizen.repo

import android.content.SharedPreferences
import com.example.kaizen.extensions.addOrRemove
import com.google.gson.Gson

object LocalStorage {
    const val FAVORITESPORTID = "FAVORITESPORTID"

    internal lateinit var favoriteSports :FavoriteSports
    fun init(sharedPrefs: SharedPreferences) {
        favoriteSports = FavoriteSports(sharedPrefs)
    }

    internal class FavoriteSports(private val sharedPrefs: SharedPreferences) {
        private var listOfFavoriteSports: MutableList<String>? = mutableListOf()
        fun setRemoveFavoriteSport(id: String) = run {
            listOfFavoriteSports = getFavoriteSport()?.toMutableList() ?: mutableListOf()
            listOfFavoriteSports?.addOrRemove(id)
            val json = Gson().toJson(listOfFavoriteSports)
            sharedPrefs.edit().putString(FAVORITESPORTID, json).apply()
        }

        fun getFavoriteSport(): List<String>? {
            val json = sharedPrefs.getString(FAVORITESPORTID,"")

            return try {
                Gson().fromJson(json, List::class.java).map {
                    if (it is String) it else ""
                }
            } catch (exception: Exception) {
                null
            }

        }
    }
}