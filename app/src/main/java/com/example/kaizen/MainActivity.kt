package com.example.kaizen

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.kaizen.composables.MainScreenView
import com.example.kaizen.repo.sealedClasses.UserActions
import com.example.kaizen.ui.theme.KaizenTheme
import com.example.kaizen.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainScreenViewModel by viewModels()
    val sharedPref = baseContext?.getSharedPreferences(
        getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    private val postAction: (UserActions) -> Unit = {
        lifecycleScope.launch {
            viewModel.userActionIntent.send(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //because i have only one view i feel like it is over kill to use an extra fragment. If i had at least 2 views I will have navigation
            KaizenTheme {
                MainScreenView(
                    viewState = viewModel.viewState.value,
                    userActions = postAction
                ).HandleState()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()

    }
}