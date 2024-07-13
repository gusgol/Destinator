package me.goldhardt.destinator.core.ai.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.goldhardt.destinator.core.ai.PromptService
import me.goldhardt.destinator.core.ai.gemini.GeminiPromptService

@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    @Provides
    fun providePromptService(): PromptService {
        return GeminiPromptService()
    }
}