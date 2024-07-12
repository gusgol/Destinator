package me.goldhardt.destinator.core.ai

/**
 * Represents the input to be processed by the AI provider
 */
interface Prompt {
    val texts: List<String>
}