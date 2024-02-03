package com.example.kaizen.dataFactories

import androidx.compose.runtime.mutableStateOf
import com.example.kaizen.TestData
import com.example.kaizen.extensions.addOrRemove
import com.example.kaizen.repo.dataclasses.MainScreenUiModel
import junit.framework.TestCase
import org.junit.Test

class MainScreenDataFactoryTest : TestCase() {

    private val testData by lazy { TestData() }

    @Test
    fun testTransformation() {
        val dataFactory = MainScreenDataFactory()
        val dataFromCall = testData.dummyData
        val dataAfterTransform = dataFactory.transformData(dataFromCall.asList())
        assertEquals(dataFromCall.size, dataAfterTransform?.size)

        dataFromCall.forEachIndexed { index, responseGetSports ->
            assertEquals(
                responseGetSports.e.size,
                dataAfterTransform!![index].matchDetails.value.size
            )
            assertEquals(responseGetSports.d, dataAfterTransform[index].sportsText)
            assertEquals(responseGetSports.i, dataAfterTransform[index].sportsId)
        }
    }

    fun testFilteringAfterFavorite() {
        val dataFactory = MainScreenDataFactory()
        val dataFromCall = testData.dummyData
        val uiModel =
            MainScreenUiModel(mutableStateOf(dataFactory.transformData(dataFromCall.asList())!!))
        val favorites = mutableListOf<String>()
        favorites.clear() // it's already clear but why not
        favorites.addOrRemove(fetchRandomIds())
        favorites.addOrRemove(fetchRandomIds())
        favorites.addOrRemove(fetchRandomIds())
        favorites.forEach {
            dataFactory.addToFavoriteTheSport(uiModel, it)
        }

        assertEquals(favorites.size, uiModel.model.value.filter { it.isFavorite.value }.size)
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