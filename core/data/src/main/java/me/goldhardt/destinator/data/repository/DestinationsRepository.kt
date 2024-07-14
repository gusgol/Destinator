package me.goldhardt.destinator.data.repository

interface DestinationsRepository {

    suspend fun createDestination(
        city: String,
        fromMs: Long,
        toMs: Long,
        tripStyleList: List<String>
    ): Result<Unit>
}