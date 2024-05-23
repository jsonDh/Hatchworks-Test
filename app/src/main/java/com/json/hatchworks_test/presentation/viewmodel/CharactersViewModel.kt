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
    private val _charactersListState = MutableStateFlow<CharactersListState>(CharactersListState.Initial)
    val charactersListState: StateFlow<CharactersListState> = _charactersListState

    fun getCharacters() {
        getCharactersList()
    }

    private fun getCharactersList() {
        Log.d(TAG, "Getting characters list...")
        _charactersListState.value = CharactersListState.Loading
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCharacterList()
                withContext(Dispatchers.Main) {
                    _charactersListState.value = CharactersListState.Success(response.data?.characters?.results)
                    Log.d(TAG, response.data?.characters?.results.toString())
                }
            } catch (e: ApolloException) {
                _charactersListState.value = CharactersListState.Error(e.message.toString())
            }
        }
    }

    companion object {
        private const val TAG = "CHARACTERS-LIST-VIEW-MODEL"
    }

}

sealed class CharactersListState {
    object Initial : CharactersListState()
    object Loading : CharactersListState()
    data class Success(val data: List<CharactersListQuery.Result?>?) : CharactersListState()
    data class Error(val message: String) : CharactersListState()
}