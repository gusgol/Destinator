package me.goldhardt.destinator.core.places.googleplaces

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import me.goldhardt.destinator.core.places.BuildConfig
import me.goldhardt.destinator.core.places.PlaceMetadata
import me.goldhardt.destinator.core.places.PlacesDataSource
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class GooglePlacesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : PlacesDataSource {

    companion object {
        private const val SEARCH_RADIUS_M = 25_000.0
    }

    private val apiKey = BuildConfig.PLACES_API_KEY
    private val client: PlacesClient

    init {
        Places.initializeWithNewPlacesApiEnabled(context, apiKey)
        client = Places.createClient(context)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getPlace(
        query: String,
        latitude: Double,
        longitude: Double
    ): PlaceMetadata? = suspendCancellableCoroutine { continuation ->
        val placeMetadataFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.PHOTO_METADATAS,
            Place.Field.ICON_URL,
            Place.Field.LAT_LNG
        )
        val searchRequest = SearchByTextRequest.builder(query, placeMetadataFields)
            .setMaxResultCount(1)
            .setLocationBias(CircularBounds.newInstance(LatLng(latitude, longitude), SEARCH_RADIUS_M))
            .build()

        client.searchByText(searchRequest)
            .addOnSuccessListener { response ->
                continuation.resume(
                    response.places.first()?.let { place ->
                        PlaceMetadata(
                            sourceId = place.id.orEmpty(),
                            iconUrl = place.iconUrl,
                            latitude = place.latLng?.latitude,
                            longitude = place.latLng?.longitude
                        )
                    },
                    onCancellation = {
                        continuation.resumeWithException(it)
                    }
                )
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }
}