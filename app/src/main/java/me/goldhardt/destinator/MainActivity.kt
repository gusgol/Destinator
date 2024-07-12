package me.goldhardt.destinator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.goldhardt.destinator.core.ai.gemini.GeminiPromptService
import me.goldhardt.destinator.core.designsystem.theme.DestinatorTheme
import me.goldhardt.destinator.data.datasource.GenerateItineraryAIDataSource
import me.goldhardt.destinator.ui.DestinatorApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DestinatorTheme {
                DestinatorApp()
            }
        }
    }

    override fun onResume() {
        val dataSource = GenerateItineraryAIDataSource(
            promptService = GeminiPromptService()
        )
        GlobalScope.launch {
            dataSource.generateItinerary(
                city = "New York",
                from = "15/7/2024",
                to = "18/7/24",
                tripStyle = "Romantic getaways, Sports"
            ).also {
                Log.e("Destinator", it.toString())
            }
        }
        super.onResume()
    }
}