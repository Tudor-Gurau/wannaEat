package com.example.wannaeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wannaeat.presentation.detailedscreen.DetailedScreen
import com.example.wannaeat.presentation.mainscreen.MainScreen
import com.example.wannaeat.presentation.ui.theme.WannaEatTheme
import com.example.wannaeat.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WannaEatTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "main_screen",
                    route = "main_graph"
                ) {
                    composable("main_screen") {
                        val viewModel: MainViewModel =
                            hiltViewModel(navController.getBackStackEntry("main_graph"))
                        MainScreen(navController = navController, viewModel = viewModel)
                    }
                    composable(
                        "detailed_screen/{id}/{favorites}",
                        arguments = listOf(
                            navArgument("id") {
                                type = NavType.StringType
                            },
                            navArgument("favorites") {
                                type = NavType.BoolType
                            }
                        )
                    ) { navBackStackEntry ->
                        val id = navBackStackEntry.arguments?.getString("id")
                        val favorites = navBackStackEntry.arguments?.getBoolean("favorites")
                        val viewModel: MainViewModel =
                            hiltViewModel(navController.getBackStackEntry("main_graph"))
                        if (id != null && favorites != null) {
                            DetailedScreen(
                                navController = navController,
                                id = id,
                                favorites = favorites,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
