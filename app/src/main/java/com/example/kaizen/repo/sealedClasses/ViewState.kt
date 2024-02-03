package com.example.kaizen.repo.sealedClasses

sealed class ViewState<out T : BaseView> {

    class Loading : ViewState<Nothing>()

    data class Loaded<out T : BaseView>(val result: T) : ViewState<T>()

    data class Error(val error: Throwable?) : ViewState<Nothing>()
}
