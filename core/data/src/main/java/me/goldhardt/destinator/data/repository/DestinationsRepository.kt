package me.goldhardt.destinator.data.repository

import me.goldhardt.destinator.data.model.destination.Destination
import kotlinx.coroutines.flow.Flow

interface DestinationsRepository {

    suspend fun createDestination(
        city: String,
        fromMs: Long,
        toMs: Long,
        tripStyleList: List<String>
    ): Result<Long>

    fun getDestination(destinationId: Long): Flow<Destination?>

    fun getDestinations(): Flow<List<Destination>>
}