package com.example.kaizen.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaizen.dataFactories.MainScreenDataFactory
import com.example.kaizen.repo.Repository
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import com.example.kaizen.repo.sealedClasses.UserActions
import com.example.kaizen.repo.sealedClasses.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataFactory: MainScreenDataFactory,
    private val repo: Repository,
) : ViewModel() {
    private val uiModel: MainScreenUiModel = MainScreenUiModel()
    val viewState = mutableStateOf<ViewState<MainScreenUiModel>>(ViewState.Loading())
    val userActionIntent = Channel<UserActions> { Channel.UNLIMITED }

    init {
        handleUsersIntent()
    }

    fun fetchData() {
        viewModelScope.launch {
            val response = makeNetworkCallGetSports()
            if (response.isSuccessful) {
                uiModel.model.value = dataFactory.transformData(response.body()) ?: listOf()
                viewState.value = ViewState.Loaded(uiModel)
            } else {
                viewState.value = ViewState.Error(Throwable(message = "error"))
            }
        }
    }

    private fun handleUsersIntent() {
        viewModelScope.launch {
            userActionIntent.consumeAsFlow().collect {
                when (it) {

                    is UserActions.FavoriteGameClicked -> {
                        dataFactory.addToFavoriteTheGame(uiModel, it.pair)
                    }

                    is UserActions.FavoriteSportClicked -> {
                        dataFactory.addToFavoriteTheSport(uiModel, it.id)
                    }

                    is UserActions.ExpandCollapseSport -> {
                        dataFactory.expandCollapseSport(uiModel, it.id)
                    }
                }
            }
        }
    }

    private suspend fun makeNetworkCallGetSports() = withContext(Dispatchers.IO) {
        return@withContext repo.getSports()
    }


}