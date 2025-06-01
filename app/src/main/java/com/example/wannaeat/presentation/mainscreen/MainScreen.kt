package com.example.wannaeat.presentation.mainscreen

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wannaeat.data.models.RecipeEntry
import com.example.wannaeat.presentation.viewmodel.MainViewModel
import com.example.wannaeat.presentation.ui.theme.Purple40
import com.example.wannaeat.presentation.uistates.MainScreenUiState
import com.example.wanneat.R


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.value
    val latestPrompt = viewModel.latestPrompt.value
    val latestFavoriteRecipeList = viewModel.latestFavoriteRecipeList

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            SearchBar(
                hint = "What do you feel like eating?",
                onSearch = { prompt -> viewModel.searchRecipe(prompt, "") },
                showFavoriteList = { viewModel.changeUiStateToIdle() },
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {
                is MainScreenUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(6.dp)
                    ) {
                        Text(
                            text = "Oops! No results..",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(R.drawable.no_results),
                            contentDescription = null
                        )
                    }
                }

                is MainScreenUiState.Idle -> {
                    if (latestFavoriteRecipeList.value.isNotEmpty()) {
                        viewModel.getFavoriteList()

                        FavoritesRecipeSection(
                            title = "Favorites",
                            navController = navController,
                            recipes = latestFavoriteRecipeList.value,
                            viewModel = viewModel,
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(6.dp)
                        ) {
                            Text(
                                text = "Let's start cooking!",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            Image(
                                modifier = Modifier.fillMaxWidth(),
                                painter = painterResource(R.drawable.start_page_01),
                                contentDescription = null
                            )
                        }

                    }
                }

                is MainScreenUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = androidx.compose.material.MaterialTheme.colors.primary,
                            modifier = Modifier.scale(2f)
                        )
                    }
                }

                is MainScreenUiState.Success -> {
                    SuggestedRecipeSection(
                        title = "Suggested recipes",
                        navController = navController,
                        recipes = uiState.recipes,
                        onClick = {
                            var allTitles = "Exclude from the search these recipes: "
                            uiState.recipes.forEach {
                                allTitles += "${it.recipeTitle} "
                            }

                            viewModel.searchRecipe(latestPrompt, exclude =  allTitles)
                        },
                        viewModel = viewModel,
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
    showFavoriteList: () -> Unit,
    viewModel: MainViewModel,
) {
    var text by viewModel.searchBarText
    var isHintDisplayed by remember { mutableStateOf(hint.isNotEmpty()) }

    Box(
        modifier = modifier
            .padding(start = 6.dp, end = 6.dp)
            .background(MaterialTheme.colorScheme.background, CircleShape)
            .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
            .padding(start = 20.dp, end = 10.dp, top = 2.dp, bottom = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        viewModel.updateSearchBarText(it)
                        if (it.isEmpty()) {
                            viewModel.getFavoriteList()
                            showFavoriteList()
                        }
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isHintDisplayed = !it.isFocused && text.isEmpty()
                        }
                        .padding(end = 32.dp) // space for icon
                )

                if (isHintDisplayed) {
                    Text(
                        text = hint,
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 16.sp)
                    )
                }
            }

            IconButton(
                onClick = { onSearch(text) },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
    }
}


@Composable
fun SuggestedRecipeSection(
    title: String,
    navController: NavController,
    recipes: List<RecipeEntry>,
    onClick: () -> Unit,
    viewModel: MainViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            recipes.forEach { recipe ->
                SuggestedRecipeCard(leavingFromFavorites = false, recipe, navController, viewModel)
            }

        }
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .width(210.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Purple40)
            ) {
                Text(
                    text = "I don't like these",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 16.sp)
                )
            }
        }

    }
}

@Composable
fun FavoritesRecipeSection(
    title: String,
    navController: NavController,
    recipes: List<RecipeEntry>,
    viewModel: MainViewModel,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            recipes.forEach { recipe ->
                FavoriteRecipeCard(leavingFromFavorites = true, recipe, navController, viewModel)
            }

        }
    }
}

@Composable
fun SuggestedRecipeCard(
    leavingFromFavorites: Boolean,
    recipe: RecipeEntry,
    navController: NavController,
    viewModel: MainViewModel
) {
    var isFavorite by remember { mutableStateOf(recipe.favorite) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp))
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(22.dp))
            .padding(end = 20.dp)
            .clickable {
                navController.navigate(
                    "detailed_screen/${recipe.id}/${leavingFromFavorites}"
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
        ) {
            Image(
                modifier = Modifier
                    .alpha(0.14f)
                    .background(Color.Gray),
                painter = painterResource(R.drawable.landscape_placeholder_svgrepo_com),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = recipe.recipeTitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = recipe.recipeTime,
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 16.sp)
            )
        }

        IconButton(
            onClick = {
                viewModel.toggleFavorite(recipe)
                viewModel.getFavoriteList()
                isFavorite = !isFavorite
            },
            modifier = Modifier.scale(1.3f)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "favorite" else "notFavorite",
                tint = if (isFavorite) Color(0xFF6A1B9A) else Color.Black,
            )
        }
    }
}

@Composable
fun FavoriteRecipeCard(
    leavingFromFavorites: Boolean,
    recipe: RecipeEntry,
    navController: NavController,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp))
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(22.dp))
            .padding(end = 20.dp)
            .clickable {
                navController.navigate(
                    "detailed_screen/${recipe.id}/${leavingFromFavorites}"
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
        ) {
            Image(
                modifier = Modifier
                    .alpha(0.14f)
                    .background(Color.Gray),
                painter = painterResource(R.drawable.landscape_placeholder_svgrepo_com),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = recipe.recipeTitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = recipe.recipeTime,
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Black, fontSize = 16.sp)
            )
        }

        IconButton(
            onClick = {
                viewModel.getFavoriteList()
                viewModel.toggleFavorite(recipe)
            },
            modifier = Modifier.scale(1.3f)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "favorite",
                tint = Color(0xFF6A1B9A),
            )
        }
    }
}