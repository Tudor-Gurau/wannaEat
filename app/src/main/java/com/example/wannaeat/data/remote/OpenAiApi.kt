package com.example.wannaeat.data.remote

import com.example.wannaeat.data.models.RecipeEntry
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiApi {

    @POST("v1/chat/completions")
    suspend fun getRecipes(@Body request: OpenAiRequest): OpenAiResponse
}