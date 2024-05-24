package com.json.hatchworks_test.presentation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.R
import com.json.hatchworks_test.presentation.ui.theme.HatchworksTestTheme
import com.json.hatchworks_test.presentation.view.Screens
import com.json.hatchworks_test.presentation.viewmodel.CharacterListViewModel
import com.json.hatchworks_test.presentation.viewmodel.CharactersListState


/**
 * Displays a list of characters.
 *
 * @param charactersViewModel ViewModel to fetch characters list.
 * @param navController Controller to manage navigation actions.
 */
@Composable
fun CharacterListFragment(charactersViewModel: CharacterListViewModel, navController: NavController) {
    // Fetch characters list if the state is initial
    LaunchedEffect(Unit) {
        charactersViewModel.charactersListState.collect { charactersState ->
            if (charactersState is CharactersListState.Initial) {
                charactersViewModel.getCharacters()
            }
        }
    }
    // Collect characters list state
    val charactersState by charactersViewModel.charactersListState.collectAsState()
    // Scaffold containing the top bar and content area
    Scaffold(
        topBar = {
            SmallAppBar()
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                // Display characters list
                ShowCharactersList(charactersState, navController)
            }
        }
    )
}


/**
 * Displays the list of characters based on the state.
 *
 * @param charactersListState Current state of the characters list.
 * @param navController Controller to manage navigation actions.
 */
@Composable
fun ShowCharactersList(charactersListState: CharactersListState?, navController: NavController) {
    when (charactersListState) {
        is CharactersListState.Initial -> EmptyList()
        is CharactersListState.Success -> {
            // Filter out null characters and display them in a lazy vertical grid
            val characters = charactersListState.data?.filterNotNull() ?: emptyList()
            if (characters.isEmpty()) {
                EmptyList()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(characters.size) { index ->
                        CharacterCard(characters[index], navController)
                    }
                }
            }
        }

        is CharactersListState.Loading -> {
            EmptyList()
        }

        is CharactersListState.Error -> ShowError(stringResource(id = R.string.characters_list_error))
        else -> {}
    }
}


/**
 * Displays a character card.
 *
 * @param character Character data to display.
 * @param navController Controller to manage navigation actions.
 */
@Composable
fun CharacterCard(character: CharactersListQuery.Result?, navController: NavController) {
    // Card modifier and shape
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(6.dp, 6.dp)
            .heightIn(0.dp, 250.dp)
            .clickable {
                // Navigate to character details screen on card click
                navController.navigate(Screens.CharacterDetailsScreen.withArgs(character?.id.toString()))
            },
        shape = CardDefaults.elevatedShape,
        // Card colors and elevation
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column {
            // Async image loading
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character?.image)
                    .crossfade(true)
                    .build(),
                contentDescription = character?.name,
                placeholder = painterResource(id = R.drawable.rick_ic),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            )
            // Character name and species
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(0.3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = character?.name.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = character?.species.toString(), fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

/**
 * Displays an empty list message.
 */
@Composable
fun EmptyList() {
    HatchworksTestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f), verticalArrangement = Arrangement.Center
        ) {
            // Placeholder image
            Image(
                painter = painterResource(id = R.drawable.rick_ic),
                contentDescription = "Empty Image View",
                modifier = Modifier.fillMaxWidth()
            )
            // Empty list message
            Text(
                text = "There is no character list at the moment but surely some will come up soon.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 50.dp),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}