package com.example.kaizen.dataFactories

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import com.example.kaizen.TestData
import com.example.kaizen.extensions.addOrRemove
import com.example.kaizen.repo.LocalStorage
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainScreenDataFactoryTest : TestCase() {


    private val testData by lazy { TestData() }
    private val testDispatcher = TestCoroutineDispatcher()
    private val mockSharedPreferences get() = mockk<SharedPreferences>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        LocalStorage.init(mockSharedPreferences)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testTransformation() {
        val dataFactory = MainScreenDataFactory()
        val dataFromCall = testData.dummyData
        val uiModel = runBlocking {
            MainScreenUiModel(mutableStateOf(dataFactory.transformData(dataFromCall.asList())!!))
        }
        assertEquals(dataFromCall.size, uiModel.model.value.size)

        dataFromCall.forEachIndexed { index, responseGetSports ->
            assertEquals(
                responseGetSports.activeEvents.size,
                uiModel.model.value[index].matchDetails.value.size
            )
            assertEquals(responseGetSports.sportName, uiModel.model.value[index].sportsText)
            assertEquals(responseGetSports.sportId, uiModel.model.value[index].sportsId)
        }
    }

    fun testFilteringAfterFavorite() {
        val dataFactory = MainScreenDataFactory()
        val dataFromCall = testData.dummyData
        val uiModel = runBlocking {
            MainScreenUiModel(mutableStateOf(dataFactory.transformData(dataFromCall.asList())!!))
        }
        val favorites = mutableListOf<String>()
        favorites.clear() // it's already clear but why not
        favorites.addOrRemove(fetchRandomIds())
        favorites.addOrRemove(fetchRandomIds())
        favorites.addOrRemove(fetchRandomIds())
        favorites.forEach {
            dataFactory.addToFavoriteTheSport(uiModel, it)
        }
        val uiFavoriteList = uiModel.model.value.filter { it.isFavorite.value }
        assertEquals(favorites.size, uiFavoriteList.size)

        favorites.forEach {forEachValue->
            assertTrue(uiFavoriteList.firstOrNull { it.sportsId?.equals(forEachValue) == true } != null)
        }
    }

    private fun fetchRandomIds(): String {
        val rnds = (0..9).random()
        return when (rnds) {
            0 -> "FOOT"
            1 -> "BASK"
            2 -> "TENN"
            3 -> "TABL"
            4 -> "VOLL"
            5 -> "ESPS"
            6 -> "ICEH"
            7 -> "HAND"
            8 -> "SNOO"
            9 -> "FUTS"
            else -> " not possible"
        }
    }


}