package com.json.hatchworks_test.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.R
import com.json.hatchworks_test.presentation.viewmodel.CharactersState

@Composable
fun ShowCharactersList(charactersState: CharactersState?) {
    when (charactersState) {
        is CharactersState.Success -> {
            val characters = charactersState.data?.filterNotNull() ?: emptyList()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(characters.size) {index ->
                    CharacterCard(characters[index])
                }
            }
        }
        is CharactersState.Loading -> ShowLoading()
        is CharactersState.Error -> ShowError(stringResource(id = R.string.characters_list_error))
        else -> {}
    }
}

@Composable
fun CharacterCard(character: CharactersListQuery.Result?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(6.dp, 6.dp)
            .heightIn(0.dp, 250.dp),
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column {
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

@Composable
@Preview
fun PreviewItems() {
    val char: CharactersListQuery.Result = CharactersListQuery.Result(
        "1", "Rick", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    )
    CharacterCard(character = char)
}