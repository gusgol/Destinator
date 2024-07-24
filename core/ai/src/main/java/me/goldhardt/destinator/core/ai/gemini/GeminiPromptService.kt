package me.goldhardt.destinator.core.ai.gemini

import me.goldhardt.destinator.core.ai.Prompt
import me.goldhardt.destinator.core.ai.PromptService
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import me.goldhardt.destinator.core.ai.BuildConfig
import me.goldhardt.destinator.core.ai.PromptException

/**
 * A prompt service that uses the Gemini AI provider
 */
class GeminiPromptService : PromptService {
    private val geminiApiKey = BuildConfig.GEMINI_API_KEY
    private val modelName = "gemini-1.5-flash"
    private val mimeType = "application/json"

    private val model = GenerativeModel(
        modelName,
        geminiApiKey,
        generationConfig = generationConfig {
            temperature = 1f
            topK = 64
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = mimeType
        }
    )

    override suspend fun process(prompt: Prompt): Result<String> {
        runCatching {
            model.generateContent(
                content {
                    prompt.texts.forEach { text(it) }
                }
            ).text?.let {
                return Result.success(it)
            }
        }
        return Result.failure(
            PromptException("Gemini failed to generate response")
        )
    }
}