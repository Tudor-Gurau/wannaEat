package com.example.wannaeat.data.repository

import com.example.wannaeat.data.models.RecipeEntry
import com.example.wannaeat.domain.RecipeRepository
import com.example.wannaeat.util.Resource

/* class MockRecipeRepository : RecipeRepository {

    override suspend fun getResponse(prompt: String): Resource<List<RecipeEntry>> {
        return if (prompt.lowercase().contains("pizza") || prompt.lowercase().contains("salad")) {
            Resource.Success(
                listOf(
                    RecipeEntry(
                        id = "12345",
                        recipeTitle = "Margherita Pizza",
                        imageUrl = "https://example.com/pizza.jpg",
                        recipeTime = "30 minutes",
                        ingredients = "Flour, Tomato, Mozzarella, Basil",
                        instructions = "1. Prepare dough\n2. Add toppings\n3. Bake"
                    ),
                    RecipeEntry(
                        id = "1645632",
                        recipeTitle = "Caesar Salad",
                        imageUrl = "https://example.com/salad.jpg",
                        recipeTime = "15 minutes",
                        ingredients = "Lettuce, Croutons, Caesar dressing",
                        instructions = "1. Mix ingredients\n2. Add dressing"
                    ),
                    RecipeEntry(
                        id = "16i54965",
                        recipeTitle = "Veggie Pizza",
                        imageUrl = "https://example.com/veggie_pizza.jpg",
                        recipeTime = "40 minutes",
                        ingredients = "Dough, Vegetables, Cheese",
                        instructions = "1. Roll dough\n2. Add toppings\n3. Bake"
                    ),
                    RecipeEntry(
                        id = "0964569043",
                        recipeTitle = "Fruit Salad",
                        imageUrl = "https://example.com/fruit_salad.jpg",
                        recipeTime = "10 minutes",
                        ingredients = "Assorted fruits, Mint, Honey",
                        instructions = "1. Chop fruits\n2. Mix and serve"
                    )
                )
            )
        } else {
            Resource.Success(emptyList())
        }
    }

    override suspend fun getFavorites(): List<RecipeEntry> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(recipe: RecipeEntry) {
        TODO("Not yet implemented")
    }
} */
