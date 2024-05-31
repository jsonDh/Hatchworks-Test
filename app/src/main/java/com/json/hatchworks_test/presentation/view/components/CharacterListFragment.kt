package com.json.hatchworks_test.presentation.view.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }
    // Scaffold containing the top bar and content area
    Scaffold(
        topBar = {
            SmallAppBar()
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                // Display characters list
                ShowCharactersList(orientation, charactersState, navController){
                    charactersViewModel.reloadAfterError()
                }
            }
        }
    )
}


/**
 * Displays the list of characters based on the state.
 *
 * @param orientation Current screen orientation.
 * @param charactersListState Current state of the characters list.
 * @param navController Controller to manage navigation actions.
 * @param onClick Click method called to retry when an error occurs
 */
@Composable
fun ShowCharactersList(orientation: Int, charactersListState: CharactersListState?, navController: NavController, onClick: () -> Unit) {
    when (charactersListState) {
        is CharactersListState.Initial -> EmptyList(orientation)
        is CharactersListState.Success -> {
            // Filter out null characters and display them in a lazy vertical grid
            val characters = charactersListState.data?.filterNotNull() ?: emptyList()
            if (characters.isEmpty()) {
                EmptyList(orientation)
            } else {
                DisplayCharactersList(orientation, characters, navController)
            }
        }

        is CharactersListState.Loading -> {
            ShowLoading()
        }

        is CharactersListState.Error -> {
            ShowError(orientation, charactersListState.message){ onClick() }
        }
        else -> {}
    }
}

/**
 * Displays the list of characters based on the device orientation.
 *
 * @param orientation Current screen orientation.
 * @param characters List of characters to display.
 * @param navController Controller to manage navigation actions.
 */
@Composable
fun DisplayCharactersList(orientation: Int, characters : List<CharactersListQuery.Result>, navController: NavController){
    when (orientation){
        Configuration.ORIENTATION_LANDSCAPE -> {
            LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.padding(80.dp, 0.dp)) {
                items(characters.size) { index ->
                    CharacterCard(characters[index], navController)
                }
            }
        }
        Configuration.ORIENTATION_PORTRAIT -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(characters.size) { index ->
                    CharacterCard(characters[index], navController)
                }
            }
        }
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
 * @param orientation Current screen orientation.
 */
@Composable
fun EmptyList(orientation: Int) {
    HatchworksTestTheme {
        when (orientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
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
                        Text(
                            text = stringResource(id = R.string.empty_list_label),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                }
            }
            Configuration.ORIENTATION_PORTRAIT -> {
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
                        text = stringResource(id = R.string.empty_list_label),
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
    }
}