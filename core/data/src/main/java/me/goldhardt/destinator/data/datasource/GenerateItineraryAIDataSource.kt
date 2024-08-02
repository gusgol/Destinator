package me.goldhardt.destinator.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.goldhardt.destinator.core.ai.Prompt
import me.goldhardt.destinator.core.ai.PromptService
import me.goldhardt.destinator.data.model.itinerary.AIGenerateItineraryResponse
import java.io.IOException
import javax.inject.Inject

class GenerateItineraryAIDataSource @Inject constructor(
    private val promptService: PromptService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GenerateItineraryDataSource {

    override suspend fun generateItinerary(
        city: String,
        from: String,
        to: String,
        tripStyle: String
    ): Result<AIGenerateItineraryResponse> {
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
                val response = Json.decodeFromString<AIGenerateItineraryResponse>(json)
                Result.success(response)
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
                "You are a travel itinerary generator. You will take in 3 parameters: city (if city is ambiguous, and there are multiple cities with the same name, state and country can be supplied), a date range (start and end, using ISO 8601 format YYYY-MM-DD), and the trip styles. You will then create a travel itinerary based on those inputs. Those places should be in the city chose, or nearby cities. It should be inside the data range (inclusive). Places and attractions should prioritize the trip style chosen. The output json should follow the following format:\n{\n      city: String,\n      country: String,\n      latitude: Decimal,\n      longitude: Decimal,\n      itinerary: [\n            {\n                  date: String,                  tripDay: Int                  name: String,\n                  description: String,\n                  longitude: Decimal,\n                  latitude: Decimal,\n                  visitTimeMin: Int\n            },\n            ...\n      ]\n}",
                "input: Create an itinerary for the city of Paris, from 2024-7-17 to 2024-7-19 (inclusive), make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of places and and attractions: Must see, Photography Spots. The response must be a json file: Include the date following ISO 8601 standard format YYYY-MM-DD, trip day (the index of the day in the trip, example: Day 1, or Day 2), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). They list hast to have attractions in all days selected. When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins) at most. Add as must places and attractions as you can.",
                "output: {    \"city\": \"Paris\",    \"country\": \"France\",    \"latitude\": 48.8567,    \"longitude\": 2.3522,    \"itinerary\": [        {            \"date\": \"2024-07-17\",            \"tripDay\": 1,            \"name\": \"Eiffel Tower\",            \"description\": \"Iconic symbol of Paris with stunning views from the top.\",            \"latitude\": 48.8583,            \"longitude\": 2.2945,            \"visitTimeMin\": 120        },        {            \"date\": \"2024-07-17\",            \"tripDay\": 1,            \"name\": \"Louvre Museum\",            \"description\": \"Home to the Mona Lisa and countless other masterpieces.\",            \"latitude\": 48.8606,            \"longitude\": 2.3376,            \"visitTimeMin\": 90        },        {            \"date\": \"2024-07-17\",            \"tripDay\": 1,            \"name\": \"Arc de Triomphe\",            \"description\": \"Offers panoramic views of the Champs-Élysées.\",            \"latitude\": 48.8738,            \"longitude\": 2.2950,            \"visitTimeMin\": 60        },        {            \"date\": \"2024-07-18\",            \"tripDay\": 2,            \"name\": \"Louvre Museum\",            \"description\": \"Kensington Gardens is one of the Royal Parks of London.\",            \"longitude\": -0.177920,            \"latitude\": 48.8606,            \"visitTimeMin\": 120        },        {            \"date\": \"2024-07-18\",            \"tripDay\": 2,            \"name\": \"Notre-Dame Cathedral\",            \"description\": \"Famous for its Gothic architecture and historical significance.\",            \"latitude\": 48.8530,            \"longitude\": 2.3498,            \"visitTimeMin\": 120        },        {            \"date\": \"2024-07-18\",            \"tripDay\": 2,            \"name\": \"Sacré-Cœur Basilica\",            \"description\": \"Located in Montmartre, offering spiritual and visual splendor.\",            \"latitude\": 48.8867,            \"longitude\": 2.3431,            \"visitTimeMin\": 120        },        {            \"date\": \"2024-07-19\",            \"tripDay\": 3,            \"name\": \"Musée d'Orsay\",            \"description\": \"Showcases an impressive collection of Impressionist art.\",            \"latitude\": 48.8600,            \"longitude\": 2.3266,            \"visitTimeMin\": 120        },        {            \"date\": \"2024-07-19\",            \"tripDay\": 3,            \"name\": \"Luxembourg Gardens\",            \"description\": \"A serene spot for a leisurely stroll and beautiful photos.\",            \"latitude\": 48.8462,            \"longitude\": 2.3372,            \"visitTimeMin\": 120        },        {            \"date\": \"2024-07-19\",            \"tripDay\": 3,            \"name\": \"Palais Garnier\",            \"description\": \"Dazzles with its opulent interiors and rich history.\",            \"latitude\": 48.8719,            \"longitude\": 2.3317,            \"visitTimeMin\": 120        }    ]}",
                "input: Create an itinerary for the city of London, from 2024-7-17 to 2024-7-19 (inclusive), make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of places and and attractions: Must see, Photography Spots. The response must be a json file: Include the date following ISO 8601 standard format YYYY-MM-DD, trip day (the index of the day in the trip, example: Day 1, or Day 2), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). They list hast to have attractions in all days selected. When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins) at most. Add as must places and attractions as you can.",
                "output: {\"city\": \"London\", \"country\": \"United Kingdom\", \"latitude\": 51.5074, \"longitude\": 0.1278, \"itinerary\": [{\"date\": \"2024-07-17\", \"tripDay\": 1, \"name\": \"Buckingham Palace\", \"description\": \"The official London residence of the British monarch, known for its iconic facade and changing of the guard ceremony.\", \"latitude\": 51.5014, \"longitude\": -0.1419, \"visitTimeMin\": 120}, {\"date\": \"2024-07-17\", \"tripDay\": 1, \"name\": \"Tower Bridge\", \"description\": \"A Victorian marvel with stunning views of the Thames and the city skyline.\", \"latitude\": 51.5055, \"longitude\": -0.0838, \"visitTimeMin\": 120}, {\"date\": \"2024-07-17\", \"tripDay\": 1, \"name\": \"Houses of Parliament\", \"description\": \"Home to the UK Parliament, featuring the iconic Big Ben clock tower and the Houses of Parliament.\", \"latitude\": 51.5007, \"longitude\": -0.1246, \"visitTimeMin\": 120}, {\"date\": \"2024-07-18\", \"tripDay\": 2, \"name\": \"The British Museum\", \"description\": \"A world-renowned museum showcasing artifacts from across history and cultures.\", \"latitude\": 51.5194, \"longitude\": -0.1278, \"visitTimeMin\": 120}, {\"date\": \"2024-07-18\", \"tripDay\": 2, \"name\": \"National Gallery\", \"description\": \"Home to a vast collection of European paintings from the 13th to 19th centuries.\", \"latitude\": 51.5074, \"longitude\": -0.1278, \"visitTimeMin\": 120}, {\"date\": \"2024-07-18\", \"tripDay\": 2, \"name\": \"St. Paul's Cathedral\", \"description\": \"A stunning example of English Baroque architecture, offering breathtaking views from the dome.\", \"latitude\": 51.5128, \"longitude\": -0.1017, \"visitTimeMin\": 120}, {\"date\": \"2024-07-19\", \"tripDay\": 3, \"name\": \"London Eye\", \"description\": \"A giant Ferris wheel offering panoramic views of London's landmarks.\", \"latitude\": 51.5033, \"longitude\": -0.1195, \"visitTimeMin\": 60}, {\"date\": \"2024-07-19\", \"tripDay\": 3, \"name\": \"Shakespeare's Globe\", \"description\": \"A reconstruction of Shakespeare's original Globe Theatre, offering immersive performances and a glimpse into Elizabethan theatre.\", \"latitude\": 51.5073, \"longitude\": -0.1097, \"visitTimeMin\": 120}, {\"date\": \"2024-07-19\", \"tripDay\": 3, \"name\": \"Hyde Park\", \"description\": \"A sprawling green space in central London, perfect for a relaxing stroll or a picnic.\", \"latitude\": 51.5081, \"longitude\": -0.1662, \"visitTimeMin\": 120}]}",
                "input: Create an itinerary for the city of New York, from 2024-8-26 to 2024-9-1 (inclusive), make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of places and and attractions: Sports, Photography Spots. The response must be a json file: Include the date following ISO 8601 standard format YYYY-MM-DD, trip day (the index of the day in the trip, example: Day 1, or Day 2), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). They list hast to have attractions in all days selected. When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins) at most. Add as must places and attractions as you can.",
                "output: {\"city\": \"New York\", \"country\": \"United States\", \"latitude\": 40.7128, \"longitude\": -74.0060, \"itinerary\": [{\"date\": \"2024-08-26\", \"tripDay\": 1, \"name\": \"Yankee Stadium\", \"description\": \"Home to the New York Yankees, a Major League Baseball team, offers tours and a glimpse into the history of the sport.\", \"latitude\": 40.8296, \"longitude\": -73.9283, \"visitTimeMin\": 120}, {\"date\": \"2024-08-26\", \"tripDay\": 1, \"name\": \"Brooklyn Bridge\", \"description\": \"Iconic suspension bridge connecting Manhattan and Brooklyn, offering breathtaking views of the city skyline.\", \"latitude\": 40.7027, \"longitude\": -73.9922, \"visitTimeMin\": 120}, {\"date\": \"2024-08-27\", \"tripDay\": 2, \"name\": \"Madison Square Garden\", \"description\": \"A legendary arena hosting a variety of events, including concerts, sporting events, and ice hockey.\", \"latitude\": 40.7505, \"longitude\": -73.9935, \"visitTimeMin\": 120}, {\"date\": \"2024-08-27\", \"tripDay\": 2, \"name\": \"Top of the Rock\", \"description\": \"Observatory atop Rockefeller Center, offering panoramic views of the city from a different perspective than the Empire State Building.\", \"latitude\": 40.7580, \"longitude\": -73.9855, \"visitTimeMin\": 120}, {\"date\": \"2024-08-28\", \"tripDay\": 3, \"name\": \"Citi Field\", \"description\": \"Home to the New York Mets, another Major League Baseball team, offering tours and a glimpse into the sport.\", \"latitude\": 40.7570, \"longitude\": -73.8427, \"visitTimeMin\": 120}, {\"date\": \"2024-08-28\", \"tripDay\": 3, \"name\": \"The High Line\", \"description\": \"Elevated park built on a former railway line, offering unique views of the city and its evolving architecture.\", \"latitude\": 40.7482, \"longitude\": -74.0034, \"visitTimeMin\": 120}, {\"date\": \"2024-08-29\", \"tripDay\": 4, \"name\": \"MetLife Stadium\", \"description\": \"Home to the New York Giants and the New York Jets, a NFL football stadium, offering tours and a glimpse into the sport.\", \"latitude\": 40.8142, \"longitude\": -74.0744, \"visitTimeMin\": 120}, {\"date\": \"2024-08-29\", \"tripDay\": 4, \"name\": \"Central Park\", \"description\": \"Vast urban park offering a variety of activities, from picnicking to cycling to simply enjoying nature.\", \"latitude\": 40.7829, \"longitude\": -73.9654, \"visitTimeMin\": 120}, {\"date\": \"2024-08-30\", \"tripDay\": 5, \"name\": \"Barclays Center\", \"description\": \"Multi-purpose arena hosting a variety of events, including concerts, sporting events, and basketball.\", \"latitude\": 40.6828, \"longitude\": -73.9763, \"visitTimeMin\": 120}, {\"date\": \"2024-08-30\", \"tripDay\": 5, \"name\": \"Governors Island\", \"description\": \"Island located in New York Harbor, offering scenic views of the Statue of Liberty and the city skyline, along with art installations and outdoor activities.\", \"latitude\": 40.6926, \"longitude\": -74.0178, \"visitTimeMin\": 120}, {\"date\": \"2024-08-31\", \"tripDay\": 6, \"name\": \"Arthur Ashe Stadium\", \"description\": \"Home to the US Open tennis tournament, offering tours and a glimpse into the world of professional tennis.\", \"latitude\": 40.7188, \"longitude\": -73.8561, \"visitTimeMin\": 120}, {\"date\": \"2024-08-31\", \"tripDay\": 6, \"name\": \"Dumbo\", \"description\": \"Historic neighborhood in Brooklyn known for its cobblestone streets and stunning views of the Manhattan Bridge.\", \"latitude\": 40.6988, \"longitude\": -73.9889, \"visitTimeMin\": 120}, {\"date\": \"2024-09-01\", \"tripDay\": 7, \"name\": \"Yankee Stadium\", \"description\": \"Home to the New York Yankees, a Major League Baseball team, offers tours and a glimpse into the history of the sport.\", \"latitude\": 40.8296, \"longitude\": -73.9283, \"visitTimeMin\": 120}]}",
                "input: Create an itinerary for the city of New York, OH, USA, from 2024-8-26 to 2024-8-27 (inclusive), make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of places and and attractions: Sports, Photography Spots. The response must be a json file: Include the date following ISO 8601 standard format YYYY-MM-DD, trip day (the index of the day in the trip, example: Day 1, or Day 2), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). They list hast to have attractions in all days selected. When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins) at most. Add as must places and attractions as you can.",
                "output: {\"city\": \"New York\", \"country\": \"United States\", \"latitude\": 41.1454, \"longitude\": -81.6864, \"itinerary\": [{\"date\": \"2024-08-26\", \"tripDay\": 1, \"name\": \"The Mall at Fairfield Commons\", \"description\": \"A large shopping mall located in Beavercreek, Ohio, with various stores and restaurants.\", \"latitude\": 39.6636, \"longitude\": -84.1029, \"visitTimeMin\": 180}, {\"date\": \"2024-08-26\", \"tripDay\": 1, \"name\": \"The Greene\", \"description\": \"An outdoor shopping and dining complex in Beavercreek, Ohio, with a variety of stores, restaurants, and entertainment options.\", \"latitude\": 39.6568, \"longitude\": -84.1101, \"visitTimeMin\": 120}, {\"date\": \"2024-08-27\", \"tripDay\": 2, \"name\": \"The National Museum of the US Air Force\", \"description\": \"Located in Dayton, Ohio, this museum showcases a vast collection of aircraft, including historical and modern military planes.\", \"latitude\": 39.7065, \"longitude\": -84.1365, \"visitTimeMin\": 180}, {\"date\": \"2024-08-27\", \"tripDay\": 2, \"name\": \"Carillon Historical Park\", \"description\": \"Located in Dayton, Ohio, this park features a collection of historical buildings, including a Victorian village and a working carousel.\", \"latitude\": 39.7694, \"longitude\": -84.1979, \"visitTimeMin\": 120}]}",
                "input: Create an itinerary for the city of $city, from $from to $to (inclusive), make sure to keep nearby attractions in the same day. I'm looking to priorizite these types of places and and attractions: $tripStyle. The response must be a json file: Include the date following ISO 8601 standard format YYYY-MM-DD, trip day (the index of the day in the trip, example: Day 1, or Day 2), name (name of the place), description (one paragraph description of the place), longitude, latitude, visitTimeMin (how long does it take to visit). They list hast to have attractions in all days selected. When sorting and choosing the amount of places per day, take in consideration the visitTimeMin, the sum of all visitTimeMin in a day should be almost 8 hours (480 mins) at most. Add as must places and attractions as you can.\"",
                "output: "
            )
    }
}