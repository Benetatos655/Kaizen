package com.example.kaizen.repo.dataclasses

data class ResponseGetSports(
    @JvmField val i: String?,
    @JvmField val d: String?,
    @JvmField val e: List<ResponseSportInfo?>
)

data class ResponseSportInfo(
    @JvmField val i: String?,
    @JvmField val si: String?,
    @JvmField val d: String?,
    @JvmField val tt: Int?
) {
    fun returnHomeCompetitor(): Pair<String, String> {
        val temp = d?.split("-")
        return Pair(temp?.get(0) ?: "", temp?.get(1) ?: "")
    }
}
