package com.example.wannaeat.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wannaeat.data.models.RecipeEntry
import com.example.wannaeat.domain.RecipeRepository
import com.example.wannaeat.presentation.uistates.MainScreenUiState
import com.example.wannaeat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: RecipeRepository,
) : ViewModel() {

    var uiState = mutableStateOf<MainScreenUiState>(MainScreenUiState.Idle)
        private set

    var latestPrompt = mutableStateOf("")
        private set

    var latestRecipeList = mutableStateOf<List<RecipeEntry>>(emptyList())
        private set

    var latestFavoriteRecipeList = mutableStateOf<List<RecipeEntry>>(emptyList())
        private set

    var searchBarText = mutableStateOf("")
        private set


    init {
        getFavoriteList()
    }


    fun updateSearchBarText(newText: String) {
        searchBarText.value = newText
    }

    fun changeUiStateToIdle() {
        viewModelScope.launch {
            uiState.value = MainScreenUiState.Idle
        }
    }

    fun searchRecipe(prompt: String, exclude: String) {
        viewModelScope.launch {
            if (prompt.isBlank()) {
                uiState.value = MainScreenUiState.Error("Please provide me a starting point")
                return@launch
            } else {
                uiState.value = MainScreenUiState.Loading
            }

            when (val result = repository.getResponse(prompt, exclude)) {
                is Resource.Success -> {
                    val recipes = result.data ?: emptyList()
                    uiState.value = if (recipes.isEmpty()) {
                        MainScreenUiState.Error("Only food related requests please <3")
                    } else {
                        latestPrompt.value = prompt
                        latestRecipeList.value = recipes
                        MainScreenUiState.Success(recipes)
                    }
                }

                is Resource.Error -> {
                    uiState.value = MainScreenUiState.Error(result.message ?: "Unknown error")
                }

                is Resource.Loading -> {
                    uiState.value = MainScreenUiState.Loading
                }
            }

        }
    }

    fun toggleFavorite(recipe: RecipeEntry) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe)
        }
    }

    fun getFavoriteList() {
        viewModelScope.launch {
            latestFavoriteRecipeList.value = repository.getFavorites()
        }
    }
}