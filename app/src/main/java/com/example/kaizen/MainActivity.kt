package com.example.kaizen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kaizen.composables.MainScreenView
import com.example.kaizen.ui.theme.KaizenTheme
import com.example.kaizen.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : MainScreenViewModel by viewModels()
    private val mainViewScreen = MainScreenView()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //because i have only one view i feel like it is over kill to use an extra fragment. If i had at least 2 views I will have navigation
            KaizenTheme {
               mainViewScreen.HandleState(viewState = viewModel.viewState.value)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KaizenTheme {
        Greeting("Android")
    }
}