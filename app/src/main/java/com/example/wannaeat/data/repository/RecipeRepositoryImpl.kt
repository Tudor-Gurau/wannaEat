package com.example.wannaeat.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.wannaeat.data.models.RecipeEntry
import com.example.wannaeat.data.models.RecipesWrapper
import com.example.wannaeat.data.remote.FunctionName
import com.example.wannaeat.data.remote.FunctionSchema
import com.example.wannaeat.data.remote.Message
import com.example.wannaeat.data.remote.OpenAiApi
import com.example.wannaeat.data.remote.OpenAiRequest
import com.example.wannaeat.data.remote.Tool
import com.example.wannaeat.data.remote.ToolChoice
import com.example.wannaeat.domain.RecipeRepository
import com.example.wannaeat.util.Resource
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val api: OpenAiApi,
    private val sharedPreferences: SharedPreferences,
) : RecipeRepository {
    private val gson = Gson()

    //prompt - used to request a starting point for the recipes
    //exclude - a concatenated list of titles to be excluded from the next search
    override suspend fun getResponse(prompt: String, exclude: String): Resource<List<RecipeEntry>> {
        return try {
            val functionSchema = createFunctionSchema(prompt, exclude)
            val request = OpenAiRequest(
                model = "gpt-4o",
                messages = listOf(
                    Message(
                        "user",
                        "Return exactly 4 randomly chosen recipes related to $prompt using the parameters defined in the function schema. Do not respond with free-form text.If $prompt is not related to food do not return anything. Don't make the instructions a numbered list. $exclude"
                    )
                ),
                temperature = 1.0,
                tools = listOf(Tool(function = functionSchema)),
                toolChoice = ToolChoice(
                    type = "function",
                    function = FunctionName("get_recipes")
                )
            )

            val apiResponse = api.getRecipes(request)
            apiResponse.choices
                .firstOrNull()?.message?.tool_calls
                ?.firstOrNull()?.function?.arguments?.let { Log.d("OpenAi response", it) }
            val jsonArgs = apiResponse.choices
                .firstOrNull()?.message?.tool_calls
                ?.firstOrNull()?.function?.arguments

            val recipes = Gson().fromJson(jsonArgs, RecipesWrapper::class.java).recipes.take(4)

            //adding a UUID for later safe-use
            recipes.map { recipeEntry ->
                recipeEntry.id = UUID.randomUUID().toString()
            }

            Resource.Success(recipes)
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.localizedMessage}")
        }
    }


    override suspend fun getFavorites(): List<RecipeEntry> {
        val json = sharedPreferences.getString("favorites", null)
        val type = object : TypeToken<List<RecipeEntry>>() {}.type
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(json, type) ?: emptyList()
        }
    }

    override suspend fun toggleFavorite(recipe: RecipeEntry) {
        val currentFavorites = getFavorites().toMutableList()
        val exists = currentFavorites.any { it.id == recipe.id }
        if (exists) {
            recipe.favorite = false
            currentFavorites.removeAll { it.id == recipe.id }
        } else {
            recipe.favorite = true
            currentFavorites.add(recipe)
        }
        saveFavorites(currentFavorites)
    }


    private suspend fun saveFavorites(favorites: List<RecipeEntry>) {
        val json = gson.toJson(favorites)
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString("favorites", json).apply()
        }
    }
}


fun createFunctionSchema(prompt: String, exclude: String): FunctionSchema {
    val schemaJson = """
    {
      "type": "object",
      "properties": {
        "recipes": {
          "type": "array",
          "items": {
            "type": "object",
            "properties": {
              "recipeTitle": { "type": "string" },
              "imageUrl": { "type": ["string", "null"] },
              "recipeTime": { "type": "string" },
              "ingredients": { "type": "string" },
              "instructions": { "type": "string" }
            },
            "required": [
              "recipeTitle",
              "recipeTime",
              "ingredients",
              "instructions"
            ]
          }
        }
      },
      "required": ["recipes"]
    }
    """.trimIndent()

    val gson = Gson()
    val parametersJson = gson.fromJson(schemaJson, JsonObject::class.java)

    return FunctionSchema(
        name = "get_recipes",
        description = "Return exactly 4 randomly chosen recipes related to $prompt using the parameters defined in the function schema. Do not respond with free-form text. If $prompt is not related to food do not return anything. Don't make the instructions a numbered list. $exclude",
        parameters = parametersJson
    )
}


