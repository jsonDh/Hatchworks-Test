package com.json.hatchworks_test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.R
import com.json.hatchworks_test.domain.repository.CharacterRepository
import com.json.hatchworks_test.utils.NetworkHelper
import com.json.hatchworks_test.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for managing character list-related operations and state.
 */
@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val networkHelper : NetworkHelper,
    private val resourceProvider: ResourceProvider
) :
    ViewModel() {

    // MutableStateFlow representing the state of the characters list
    private val _charactersListState = MutableStateFlow<CharactersListState>(CharactersListState.Initial)
    val charactersListState: StateFlow<CharactersListState> = _charactersListState

    /**
     * Retrieves the list of characters.
     */
    fun getCharacters() {
        getCharactersList()
    }

    /**
     * Just set the state as Initial so the whole flow will run again to try and load the list
     */
    fun reloadAfterError(){
        _charactersListState.value = CharactersListState.Initial
    }

    /**
     * Fetches the list of characters from the repository.
     */
    private fun getCharactersList() {
        Timber.tag(TAG).d("Getting characters list...")
        _charactersListState.value = CharactersListState.Loading
        viewModelScope.launch {
            delay(500) // Just a small delay to provide better user experience between the empty views and the actual data
            if (!networkHelper.isInternetAvailable()){
                _charactersListState.value = CharactersListState.Error(resourceProvider.getString(R.string.internet_error))
            } else {
                try {
                    val response = repository.getCharacterList()
                    withContext(Dispatchers.IO) {
                        _charactersListState.value = CharactersListState.Success(response.data?.characters?.results)
                        Timber.tag(TAG).d(response.data?.characters?.results.toString())
                    }
                } catch (e: Exception){
                    _charactersListState.value = CharactersListState.Error(resourceProvider.getString(R.string.characters_list_error))
                }
            }
        }
    }

    companion object {
        private const val TAG = "CHARACTERS-LIST-VIEW-MODEL"
    }

}

/**
 * Sealed class representing the various states of the characters list.
 */
sealed class CharactersListState {
    /**
     * Initial state indicating no characters have been loaded yet.
     */
    data object Initial : CharactersListState()
    /**
     * State indicating characters are currently being loaded.
     */
    data object Loading : CharactersListState()
    /**
     * State indicating successful loading of characters with the provided data.
     *
     * @param data The list of characters.
     */
    data class Success(val data: List<CharactersListQuery.Result?>?) : CharactersListState()
    /**
     * State indicating an error occurred while loading characters.
     *
     * @param message The error message.
     */
    data class Error(val message: String) : CharactersListState()
}