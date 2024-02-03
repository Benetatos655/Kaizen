package com.example.kaizen.composables

import androidx.compose.runtime.Composable
import com.example.kaizen.repo.sealedClasses.BaseView
import com.example.kaizen.repo.sealedClasses.ViewState

open class BaseViewFactory<T: BaseView>(){
    @Composable
    open fun HandleState(viewState: ViewState<T>){

    }
}