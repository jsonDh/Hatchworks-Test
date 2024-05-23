package com.json.hatchworks_test.presentation.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import com.json.hatchworks_test.R
import com.json.hatchworks_test.presentation.viewmodel.CharactersViewModel
import com.json.hatchworks_test.presentation.ui.theme.HatchworksTestTheme
import com.json.hatchworks_test.presentation.view.components.ShowCharactersList
import com.json.hatchworks_test.presentation.viewmodel.CharactersListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersListActivity : ComponentActivity() {

    private val charactersViewModel: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HatchworksTestTheme {
                CharacterListScreen(charactersViewModel)
            }
        }
        lifecycleScope.launch {
            charactersViewModel.charactersListState.collect { charactersState ->
                if (charactersState is CharactersListState.Initial) {
                    charactersViewModel.getCharacters()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(charactersViewModel: CharactersViewModel) {
    val charactersState by charactersViewModel.charactersListState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ShowCharactersList(charactersState)
            }
        }
    )
}