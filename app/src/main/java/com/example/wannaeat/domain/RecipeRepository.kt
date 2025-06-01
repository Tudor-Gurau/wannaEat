package com.example.wannaeat.domain

import com.example.wannaeat.data.models.RecipeEntry
import com.example.wannaeat.util.Resource

interface RecipeRepository {
    suspend fun getResponse(prompt: String, exclude: String): Resource<List<RecipeEntry>>

    suspend fun getFavorites(): List<RecipeEntry>

    suspend fun toggleFavorite(recipe: RecipeEntry)
}
