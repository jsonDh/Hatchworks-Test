package com.json.hatchworks_test.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: CharacterRepository) : ViewModel() {

    private var job: Job? = null
    private val _charactersState = MutableStateFlow<CharactersState>(CharactersState.Initial)
    val charactersState: StateFlow<CharactersState> = _charactersState

    fun getCharacters() {
        getCharactersList()
    }

    private fun getCharactersList() {
        Log.d(TAG, "Getting characters list...")
        _charactersState.value = CharactersState.Loading
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCharacterList()
                withContext(Dispatchers.Main) {
                    _charactersState.value = CharactersState.Success(response.data?.characters?.results)
                    Log.d(TAG, response.data?.characters?.results.toString())
                }
            } catch (e: ApolloException) {
                _charactersState.value = CharactersState.Error(e.message.toString())
            }
        }
    }

    companion object {
        private const val TAG = "CHARACTERS-VIEW-MODEL"
    }

}

sealed class CharactersState {
    object Initial : CharactersState()
    object Loading : CharactersState()
    data class Success(val data: List<CharactersListQuery.Result?>?) : CharactersState()
    data class Error(val message: String) : CharactersState()
}