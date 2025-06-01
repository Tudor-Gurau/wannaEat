package com.example.wannaeat.presentation.uistates

import com.example.wannaeat.data.models.RecipeEntry

sealed class MainScreenUiState {
    object Loading: MainScreenUiState()
    data class Success(val recipes: List<RecipeEntry>): MainScreenUiState()
    data class Error(val message: String) : MainScreenUiState()
    object Idle: MainScreenUiState()
}

