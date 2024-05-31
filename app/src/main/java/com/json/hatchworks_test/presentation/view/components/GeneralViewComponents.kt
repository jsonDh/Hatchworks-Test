@file:OptIn(ExperimentalMaterial3Api::class)

package com.json.hatchworks_test.presentation.view.components

import android.content.res.Configuration
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.json.hatchworks_test.R
import com.json.hatchworks_test.presentation.ui.theme.HatchworksTestTheme


/**
 * Handles back press events within a composable.
 *
 * @param backPressedDispatcher OnBackPressedDispatcher to handle back press events.
 * @param onBackPressed Callback function to execute when back press event occurs.
 */
@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}


/**
 * Displays a small app bar with a title.
 */
@Composable
fun SmallAppBar(){
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


/**
 * Displays a snackbar with an error message.
 *
 * @param orientation Current screen orientation.
 * @param message The error message to display.
 */
@Composable
fun ShowError(orientation : Int, message: String, onClick: () -> Unit) {
    HatchworksTestTheme {
        when (orientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Placeholder image
                    Image(
                        painter = painterResource(id = R.drawable.rick_ic),
                        contentDescription = "Empty Image View",
                        modifier = Modifier.weight(0.5f).padding(0.dp, 10.dp)
                    )
                    // Empty list message
                    Column (
                        modifier = Modifier.weight(0.5f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Error message
                        Text(
                            text = message,
                            modifier = Modifier.width(300.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        // Retry button
                        Button(
                            modifier = Modifier.width(300.dp).padding(0.dp, 10.dp),
                            onClick = {onClick()}
                        ) {
                            Text(text = stringResource(id = R.string.try_again_btn))
                        }
                    }
                }
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Placeholder image
                    Image(
                        painter = painterResource(id = R.drawable.rick_ic),
                        contentDescription = "Empty Image View",
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Error message
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 50.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    // Retry button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 50.dp),
                        onClick = {onClick()}
                    ) {
                        Text(text = stringResource(id = R.string.try_again_btn))
                    }
                }
            }
        }
    }
}

/**
 * Displays a loading progress
 *
 */
@Composable
fun ShowLoading(){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        CircularProgressIndicator(
            modifier = Modifier.width(50.dp),
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            trackColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}