package com.example.kaizen

import android.app.Application
import android.content.Context
import com.example.kaizen.repo.LocalStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KaizenAssigment : Application() {

    override fun onCreate() {
        super.onCreate()
        LocalStorage.init(
            getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
        )
    }
}