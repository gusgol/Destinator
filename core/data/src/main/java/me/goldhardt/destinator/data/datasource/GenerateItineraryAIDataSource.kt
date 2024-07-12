package me.goldhardt.destinator.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.goldhardt.destinator.core.ai.Prompt
import me.goldhardt.destinator.core.ai.PromptService
import me.goldhardt.destinator.data.model.GenerateItineraryResponse
import me.goldhardt.destinator.data.model.ItineraryItem
import java.io.IOException

class GenerateItineraryAIDataSource(
    private val promptService: PromptService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GenerateItineraryDataSource {

    override suspend fun generateItinerary(
        city: String,
        from: String,
        to: String,
        tripStyle: String
    ): Result<List<ItineraryItem>> {
        val responseJson = withContext(ioDispatcher) {
            promptService.process(
                GenerateItineraryPrompt(
                    city = city,
                    from = from,
                    to = to,
                    tripStyle = tripStyle
                )
            )
        }
        return try {
            responseJson.getOrNull()?.let { json ->
                val response = Json.decodeFromString<GenerateItineraryResponse>(json)
                Result.success(response.itinerary)
            } ?: Result.failure(
                IOException("Failed to generate itinerary.")
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private class GenerateItineraryPrompt(
        private val city: String,
        private val from: String,
        private val to: String,
        private val tripStyle: String
    ) : Prompt {
        override val texts: List<String>
            get() = listOf(
                "You are a travel itinerary generator. You will take in 3 parameters: city, a date range (start and end, using DD/MM/YYYY format), and the trip style. You will then output a json file with a list of places based on the parameters provided, separated by date (try to keep nearby places in the same date). The output json should follow the following format:\n\n{\n      city: String,\n      country: String,\n      latitude: Decimal,\n      longitude: Decimal,\n      itinerary: [\n            {\n                  date: Long\n                  name: String,\n                  description: String,\n                  longitude: Decimal,\n                  latitude: Decimal,\n                  visitTimeMin: Int\n            },\n            ...\n      ]\n}",
                "input: Create an itinerary for the city of Paris, from 10/7/2024 to 11/7/24, make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of attractions: Must-see, romantic gataways, family-friendly. The response must be a json file: Include the date (long timestamp in GMT), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins), so make sure you fill the day with places.",
                "output: {      \"city\": \"Paris\",      \"country\": \"France\",      \"latitude\": 48.864716,      \"longitude\": 2.349014,      \"itinerary\": [            {                  \"date\": 1720569600000,                  \"name\": \"Eiffel Tower\",                  \"description\": \"Description of the tower\",                  \"longitude\": 48.864716,                  \"latitude\": 2.349014,                  \"visitTimeMin\": 180            },            {                  \"date\": 1720569600000,                  \"name\": \"Arch of Triumph\",                  \"description\": \"Description of the arch\",                  \"longitude\": 48.864716,                  \"latitude\": 2.349014,                  \"visitTimeMin\": 60            },            {                  \"date\": 1720656000000,                  \"name\": \"Louvre Museum\",                  \"description\": \"Description of the museum\",                  \"longitude\": 48.864716,                  \"latitude\": 2.349014,                  \"visitTimeMin\": 240            },            {                  \"date\": 1720656000000,                  \"name\": \"Notre Dame Cathedral\",                  \"description\": \"Description of the cathedral\",                  \"longitude\": 48.864716,                  \"latitude\": 2.349014,                  \"visitTimeMin\": 120            }      ]}",
                "input: Create an itinerary for the city of $city, from $from to $to, make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of attractions: $tripStyle. The response must be a json file: Include the date (long timestamp in GMT), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins), so make sure you fill the day with places.",
                "output: "
            )
    }
}