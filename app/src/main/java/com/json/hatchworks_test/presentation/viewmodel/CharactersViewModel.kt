package com.json.hatchworks_test.presentation.viewmodel

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: CharacterRepository) :
    ViewModel() {

    private val _charactersState = MutableLiveData<CharactersState>()
    val charactersState : LiveData<CharactersState> get() = _charactersState
    private var job: Job? = null

    fun getCharacters() {
        getCharactersList()
    }

    private fun getCharactersList() {
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCharacterList()
                _charactersState.value = CharactersState.OnSuccess(response.data?.characters?.results)
            } catch (e: ApolloException){
                _charactersState.value = CharactersState.OnError(e.message.toString())
            }
        }
    }

    companion object {
        private const val TAG = "CHARACTERS-VIEW-MODEL"
    }

}

sealed class CharactersState {
    object OnLoading: CharactersState()
    data class OnSuccess(val data : List<CharactersListQuery.Result?>?) : CharactersState()
    data class OnError(val message: String) : CharactersState()
}