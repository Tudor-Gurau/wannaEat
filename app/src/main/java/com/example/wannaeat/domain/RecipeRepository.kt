package com.example.wannaeat.domain

import com.example.wannaeat.data.models.RecipeEntry
import com.example.wannaeat.data.remote.OpenAiApi
import com.example.wannaeat.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

interface RecipeRepository {
    suspend fun getResponse(prompt: String, exclude: String): Resource<List<RecipeEntry>>

    suspend fun getFavorites(): List<RecipeEntry>

    suspend fun toggleFavorite(recipe: RecipeEntry)
}
