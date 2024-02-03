package com.example.kaizen.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaizen.dataFactories.MainScreenDataFactory
import com.example.kaizen.repo.Repository
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import com.example.kaizen.repo.dataclasses.ResponseGetSports
import com.example.kaizen.repo.sealedClasses.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataFactory: MainScreenDataFactory,
    private val repo: Repository,
) : ViewModel() {
    val uiModel: MainScreenUiModel = MainScreenUiModel()
    val viewState = mutableStateOf<ViewState<MainScreenUiModel>>(ViewState.Loading())

    fun fetchData() {
        viewModelScope.launch {
            val response = repo.getSports()
            Log.d(" call response ", response.toString())
            if (response.isSuccessful) {
                uiModel.model.value = dataFactory.transformData(response.body()) ?: listOf()
                viewState.value = ViewState.Loaded(uiModel)
            }
        }
    }
}