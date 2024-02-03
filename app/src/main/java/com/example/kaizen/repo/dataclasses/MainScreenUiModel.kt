package com.example.kaizen.repo.dataclasses

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.kaizen.repo.sealedClasses.BaseView

data class MainScreenUiModel(
    var model: MutableState<List<MainScreenDataClass>> = mutableStateOf(listOf())
) : BaseView