@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.json.hatchworks_test.presentation.view.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.R
import com.json.hatchworks_test.presentation.viewmodel.CharacterState
import com.json.hatchworks_test.presentation.viewmodel.CharacterViewModel
import com.json.hatchworks_test.utils.convertDateStringToReadable

/**
 * Displays the details of a character.
 *
 * @param characterId ID of the character to display details for.
 * @param characterViewModel ViewModel to fetch character details.
 * @param navController Controller to manage navigation actions.
 */
@Composable
fun CharacterDetailFragment(
    characterId: String,
    characterViewModel: CharacterViewModel,
    navController: NavController
) {
    // Collect and handle character state
    LaunchedEffect(Unit) {
        characterViewModel.clearData()
        characterViewModel.characterState.collect { charactersState ->
            if (charactersState is CharacterState.Initial) {
                characterViewModel.getCharacterDetails(characterId)
            }
        }
    }

    // Handle back press and screen rotation
    val goBack = {
        navController.popBackStack()
        characterViewModel.clearData()
    }
    BackPressHandler(onBackPressed = goBack)
    //Handle screen rotation
    val charactersState by characterViewModel.characterState.collectAsState()
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }
    Scaffold(
        topBar = {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                            characterViewModel.clearData()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back Button"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                CharacterDetailStates(orientation, charactersState) {
                    characterViewModel.clearData()
                }
            }
        }
    )
}


/**
 * Determines the UI state of the character details screen based on the character state.
 *
 * @param orientation Current screen orientation.
 * @param characterState Current state of character details.
 * @param onClick Click method called to retry when an error occurs
 */
@Composable
fun CharacterDetailStates(orientation: Int, characterState: CharacterState, onClick : () -> Unit) {
    when (characterState) {
        is CharacterState.Initial -> {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                EmptyViewLandscape()
            } else {
                EmptyView()
            }
        }
        is CharacterState.Success -> {
            val character = characterState.data?.character
            if (character != null)
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ShowCharacterDetailsLandscape(character = character)
                } else {
                    ShowCharacterDetails(character = character)
                }
        }

        is CharacterState.Loading -> ShowLoading()

        is CharacterState.Error -> ShowError(orientation, message = characterState.message){ onClick() }
    }
}

/**
 * Displays the character details in portrait orientation.
 *
 * @param character Character data to display.
 */
@Composable
fun ShowCharacterDetails(character: CharacterQuery.Character) {
    Column {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Character image card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(86.dp, 26.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.name,
                    placeholder = painterResource(id = R.drawable.rick_ic),
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Character name and species
            Text(
                text = character.name.toString(),
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = character.species.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 12.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
            )
        }
        // Character details
        Column {
            val date = character.created.toString().convertDateStringToReadable()
            DetailData(
                image = R.drawable.heart,
                property = stringResource(id = R.string.character_detail_status_label),
                detail = character.status.toString()
            )
            DetailData(
                image = R.drawable.gender,
                property = stringResource(id = R.string.character_detail_gender_label),
                detail = character.gender.toString()
            )
            DetailData(
                image = R.drawable.date,
                property = stringResource(id = R.string.character_detail_created_label),
                detail = date
            )
        }
    }
}


/**
 * Displays a placeholder view when character details are loading (portrait orientation).
 */
@Composable
fun EmptyView() {
    Column {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder image card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(86.dp, 26.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                    .alpha(0.5f),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rick_ic),
                    contentDescription = "Default Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.empty_details_label),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 150.dp)
                    .alpha(0.5f),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}


/**
 * Displays a placeholder view when character details are loading (landscape orientation).
 */
@Composable
fun EmptyViewLandscape() {
    Row {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(120.dp, 220.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                        .alpha(0.5f),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rick_ic),
                        contentDescription = "Default Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.empty_details_label),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 250.dp)
                    .alpha(0.5f),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}


/**
 * Displays the character details in landscape orientation.
 *
 * @param character Character data to display.
 */
@Composable
fun ShowCharacterDetailsLandscape(character: CharacterQuery.Character) {
    Row {
        Column(
            modifier = Modifier
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .padding(120.dp, 26.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(character.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = character.name,
                        placeholder = painterResource(id = R.drawable.rick_ic),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(
                modifier = Modifier.weight(0.5f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = character.name.toString(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = character.species.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 12.dp),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            val date = character.created.toString().convertDateStringToReadable().toString()
            DetailData(
                image = R.drawable.heart,
                property = "Status",
                detail = character.status.toString()
            )
            DetailData(
                image = R.drawable.gender,
                property = "Gender",
                detail = character.gender.toString()
            )
            DetailData(image = R.drawable.date, property = "Created", detail = date)
        }
    }
}


/**
 * Displays detailed information of a character property.
 *
 * @param image Resource ID of the image to display.
 * @param property Name of the property.
 * @param detail Detail information of the property.
 */
@Composable
fun DetailData(image: Int, property: String, detail: String) {
    val capitalizeProperty = property.replaceFirstChar { it.uppercase() }
    Row(
        modifier = Modifier
            .heightIn(0.dp, 80.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.padding(22.dp),
                painter = painterResource(id = image),
                contentDescription = property
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = capitalizeProperty, fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = detail,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
    Divider(
        modifier = Modifier.padding(start = 70.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
    )
}