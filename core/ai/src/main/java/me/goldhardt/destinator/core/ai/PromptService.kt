package me.goldhardt.destinator.core.ai

interface PromptService {
    /**
     * Processes the given prompt and returns the response
     */
    suspend fun process(prompt: Prompt): Result<String>
}