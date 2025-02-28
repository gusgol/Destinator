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
        private const val MAX_PHOTOS = 5
        private val DEFAULT_METADATA_FIELDS = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.PHOTO_METADATAS,
            Place.Field.ICON_MASK_URL,
            Place.Field.LOCATION,
            Place.Field.EDITORIAL_SUMMARY,
            Place.Field.FORMATTED_ADDRESS
        )
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
        val searchRequest = SearchByTextRequest.builder(query, DEFAULT_METADATA_FIELDS)
            .setMaxResultCount(1)
            .setLocationBias(CircularBounds.newInstance(LatLng(latitude, longitude), SEARCH_RADIUS_M))
            .build()

        client.searchByText(searchRequest)
            .addOnSuccessListener { response ->
                continuation.resume(
                    response.places.first()?.toPlaceMetadata(),
                    onCancellation = {
                        continuation.resumeWithException(it)
                    }
                )
            }
            .addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    override suspend fun getPlaces(
        query: String,
        latitude: Double,
        longitude: Double,
    ): List<PlaceMetadata> = suspendCancellableCoroutine { continuation ->
        val searchRequest = SearchByTextRequest.builder(query, DEFAULT_METADATA_FIELDS)
            .setMaxResultCount(10)
            .setLocationBias(
                CircularBounds.newInstance(
                    LatLng(latitude, longitude),
                    SEARCH_RADIUS_M
                )
            )
            .build()

        client.searchByText(searchRequest)
            .addOnSuccessListener { response ->
                continuation.resume(
                    response.places.map { place ->
                        place.toPlaceMetadata()
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

    private fun Place.toPlaceMetadata(): PlaceMetadata {
        val photos = this.photoMetadatas?.take(MAX_PHOTOS)
        this.displayName
        return PlaceMetadata(
            sourceId = this.id.orEmpty(),
            iconUrl = this.iconMaskUrl,
            latitude = this.location?.latitude,
            longitude = this.location?.longitude,
            photosReferences = photos?.map { photoMetadata ->
                photoMetadata.zza()
            } ?: listOf(),
            displayName = this.displayName,
            description = this.editorialSummary,
            address = this.formattedAddress,
        )
    }
}