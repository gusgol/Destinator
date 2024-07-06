package me.goldhardt.destinator.feature.trips.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.goldhardt.destinator.feature.trips.R

private enum class CreateTripStep {
    EnterDestination,
    SelectDates,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen() {
    var step by rememberSaveable {
        mutableStateOf(CreateTripStep.EnterDestination)
    }
    when (step) {
        CreateTripStep.EnterDestination -> EnterDestination {
            step = CreateTripStep.SelectDates
        }
        CreateTripStep.SelectDates -> SelectDates()
    }
}

@Composable
fun EnterDestination(
    onNextClick: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(
            text = stringResource(R.string.title_select_destination_headline),
            style = MaterialTheme.typography.headlineSmall,
        )
        TextField(
            value = text,
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            onValueChange = { text = it },
            placeholder = {
                Text(
                    stringResource(R.string.hint_enter_destination),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = backgroundColor,
                focusedContainerColor = backgroundColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = onNextClick,
            Modifier.padding(48.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                stringResource(R.string.cd_select_destination)
            )
        }
    }
}

@Composable
fun SelectDates() {
    Text(text = "Select dates")
}