package com.json.hatchworks_test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
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
 * ViewModel responsible for managing character-related operations and maintaining the state of a single character.
 * It interacts with the repository to fetch character details and exposes the state using a StateFlow.
 */
@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepository) :
    ViewModel() {

    // StateFlow representing the state of the character
    private val _characterState = MutableStateFlow<CharacterState>(CharacterState.Initial)
    val characterState: StateFlow<CharacterState> = _characterState

    /**
     * Clears the character state.
     */
    fun clearData() {
        _characterState.value = CharacterState.Initial
    }

    /**
     * Retrieves the details of a character by its ID.
     */
    fun getCharacterDetails(characterId: String) {
        fetchCharacterDetails(characterId)
    }

    /**
     * Collects and logs the current state of the character.
     */
    suspend fun listen() {
        characterState.collect {
            Timber.tag(TAG).d("State is %s", it)
        }
    }

    private fun fetchCharacterDetails(characterId: String) {
        _characterState.value = CharacterState.Loading
        viewModelScope.launch {
            try {
                delay(500) // Just a small delay to provide better user experience between the empty views and the actual data
                val response = repository.getCharacterDetails(characterId)
                withContext(Dispatchers.IO) {
                    _characterState.value = CharacterState.Success(response.data)
                }
            } catch (e: ApolloException) {
                _characterState.value = CharacterState.Error(e.message.toString())
            } catch (e: Exception) {
                _characterState.value = CharacterState.Error(e.message.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }

    companion object {
        private const val TAG = "CHARACTER-VIEW-MODEL"
    }

}

sealed class CharacterState {
    data object Initial : CharacterState()
    data object Loading : CharacterState()
    data class Success(val data: CharacterQuery.Data?) : CharacterState()
    data class Error(val message: String) : CharacterState()
}