package me.goldhardt.destinator.feature.trips.destinations.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.goldhardt.destinator.data.repository.DestinationsRepository
import me.goldhardt.destinator.data.repository.GenerateItineraryRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateDestinationViewModel @Inject constructor(
    private val destinationsRepository: DestinationsRepository
) : ViewModel() {

    var city: String = ""
    var fromMs: Long = 0
    var toMs: Long = 0
    var tripStyle: List<String> = listOf()

    fun generate() {
        val prompt = "Generating itinerary for $city from ${Date(fromMs)} to ${Date(toMs)} with trip styles $tripStyle"
        Log.e("ViewModel", prompt)
        viewModelScope.launch {
            val result = destinationsRepository.createDestination(
                city = city,
                fromMs = fromMs,
                toMs = toMs,
                tripStyleList = tripStyle
            )
            Log.e("ViewModel", result.toString())
        }
    }
}