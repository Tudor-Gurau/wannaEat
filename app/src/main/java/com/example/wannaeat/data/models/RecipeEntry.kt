package com.example.wannaeat.data.models

import java.util.UUID

class RecipeEntry(
    var favorite: Boolean = false,
    var id: String,
    val recipeTitle: String,
    val imageUrl: String?,
    val recipeTime: String,
    val ingredients: String,
    val instructions: String,
)