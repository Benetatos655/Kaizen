package com.example.kaizen.repo.dataclasses

import com.google.gson.annotations.SerializedName

data class ResponseGetSports(
    @JvmField @SerializedName("i") val sportId: String?,
    @JvmField @SerializedName("d") val sportName: String?,
    @JvmField @SerializedName("e") val activeEvents: List<ResponseSportInfo?>
)

data class ResponseSportInfo(
    @JvmField @SerializedName("i") val eventId: String?,
    @JvmField @SerializedName("si") val sportId: String?,
    @JvmField @SerializedName("d") val eventName: String?,
    @JvmField @SerializedName("tt") val eventStartTime: Long?
) {
    fun returnHomeCompetitor(): Pair<String, String> {
        val temp = eventName?.split("-")
        return Pair(temp?.get(0) ?: "", temp?.get(1) ?: "")
    }
}
