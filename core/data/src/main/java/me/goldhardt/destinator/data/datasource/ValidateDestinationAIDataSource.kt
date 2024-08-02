package me.goldhardt.destinator.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.goldhardt.destinator.core.ai.Prompt
import me.goldhardt.destinator.core.ai.PromptService
import me.goldhardt.destinator.data.model.validation.ValidateDestinationResponse
import java.io.IOException
import javax.inject.Inject

class ValidateDestinationAIDataSource @Inject constructor(
    private val promptService: PromptService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ValidateDestinationDataSource {

    override suspend fun validate(query: String): Result<ValidateDestinationResponse> {
        val responseJson = withContext(ioDispatcher) {
            promptService.process(
                ValidateDestinationPrompt(query = query)
            )
        }
        return try {
            responseJson.getOrNull()?.let { json ->
                val response = Json.decodeFromString<ValidateDestinationResponse>(json)
                Result.success(response)
            } ?: Result.failure(
                IOException("Failed validate destination.")
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private class ValidateDestinationPrompt(
        private val query: String,
    ) : Prompt {
        override val texts: List<String>
            get() = listOf(
                "You are a city field validator. The city has to be validate if it's is correctly spelled and if extists, as well as if multiple cities exist with the same name or similar spelling, you will provide possible options with corrections (order by likelyhood of being correct), with a cap of 5 possibilities per prompt. It will be provided to you a city query - this can contain the city name, but also other information, such as state. You will output a JSON with the response following the schema below. \n\n{\n      result: String // possible values: success, multiple, non_existent\n      options: [ // only filled in case of result == multiple\n           {\n                  city: String,\n                  state: String,\n                  contry: String\n           }\n      ]\n}",
                "output: {\"result\": \"non_existent\", \"options\": []}",
                "validate: New yok",
                "output: {\"result\": \"multiple\", \"options\": [{\"city\": \"New York\", \"state\": \"NY\", \"country\": \"USA\"}, {\"city\": \"New York\", \"state\": \"PA\", \"country\": \"USA\"}, {\"city\": \"New York\", \"state\": \"NJ\", \"country\": \"USA\"}, {\"city\": \"New York\", \"state\": \"OH\", \"country\": \"USA\"}, {\"city\": \"New York\", \"state\": \"CT\", \"country\": \"USA\"}]}",
                "Paris",
                "output: {\"result\": \"success\", \"options\": []}",
                "London",
                "output: {\"result\": \"success\", \"options\": []}",
                "City that does not exist",
                "output: {\"result\": \"non_existent\", \"options\": []}",
                query,
                "output: ",
            )
    }
}