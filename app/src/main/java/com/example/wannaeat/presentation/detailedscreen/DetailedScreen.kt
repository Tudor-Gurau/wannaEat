package com.example.wannaeat.presentation.detailedscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wannaeat.presentation.viewmodel.MainViewModel
import com.example.wanneat.R

@Composable
fun DetailedScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    id: String,
    favorites: Boolean,
) {
    val latestRecipeList = viewModel.latestRecipeList.value
    val favoritesList = viewModel.latestFavoriteRecipeList.value
    val selectedRecipe = if (favorites) {
        favoritesList.find { it.id == id }
    } else {
        latestRecipeList.find { it.id == id }
    }
    val scrollState = rememberScrollState()
    var isFavorite by remember { mutableStateOf(selectedRecipe!!.favorite) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(
                    Color.LightGray,
                )
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.14f)
                    .background(Color.Gray),
                painter = painterResource(R.drawable.landscape_placeholder_svgrepo_com),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(10.dp)
                    .scale(1.6f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (selectedRecipe != null) {
                    Text(
                        text = selectedRecipe.recipeTitle,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 30.sp
                        )
                    )
                }
                if (selectedRecipe != null) {
                    Text(
                        text = selectedRecipe.recipeTime,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 18.sp)
                    )
                }
            }
            IconButton(
                onClick = {
                    viewModel.toggleFavorite(selectedRecipe!!)
                    isFavorite = !isFavorite
                },
                modifier = Modifier.scale(1.3f)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color(0xFF6A1B9A),
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (selectedRecipe != null) {
                IngredientsView(selectedRecipe.ingredients)
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (selectedRecipe != null) {
                InstructionsView(selectedRecipe.instructions)
            }
        }
    }
}

@Composable
fun IngredientsView(ingredients: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ingredients.split(",").forEach { item ->
            Text(
                text = "•  ${item.trim()}",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun InstructionsView(instructions: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Instructions:",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        instructions.split(Regex("""(?<=\.)\s+""")).forEachIndexed() { index, step ->
            Row {
                Text(
                    text = "${index + 1}. ",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = step.trim(),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}
