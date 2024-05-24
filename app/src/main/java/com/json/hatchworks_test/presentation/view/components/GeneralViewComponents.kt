@file:OptIn(ExperimentalMaterial3Api::class)

package com.json.hatchworks_test.presentation.view.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import com.json.hatchworks_test.R


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
 * @param message The error message to display.
 */
@Composable
fun ShowError(message: String) {
    val snackBarState = remember { SnackbarHostState() }
    snackBarState.currentSnackbarData?.dismiss()
    SnackbarHost(
        hostState = snackBarState,
        snackbar = {
            Snackbar(
                content = {
                    Text(text = message)
                }
            )
        }
    )
}