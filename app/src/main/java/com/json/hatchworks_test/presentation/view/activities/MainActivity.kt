package com.json.hatchworks_test.presentation.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.json.hatchworks_test.presentation.ui.theme.HatchworksTestTheme
import com.json.hatchworks_test.presentation.view.Screens
import com.json.hatchworks_test.presentation.view.components.CharacterDetailFragment
import com.json.hatchworks_test.presentation.view.components.CharacterListFragment
import com.json.hatchworks_test.presentation.viewmodel.CharacterListViewModel
import com.json.hatchworks_test.presentation.viewmodel.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    // ViewModel for character details
    private val detailsViewModel: CharacterViewModel by viewModels()

    // ViewModel for character list
    private val listViewModel: CharacterListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Applying the app theme
            HatchworksTestTheme {
                // Create NavController for managing navigation
                val navController = rememberNavController()

                // Setting up the NavHost with the initial route
                NavHost(
                    navController = navController,
                    startDestination = Screens.CharacterListScreen.route
                ) {

                    // Composable for character list screen
                    composable(Screens.CharacterListScreen.route) {
                        CharacterListFragment(
                            charactersViewModel = listViewModel,
                            navController = navController
                        )
                    }

                    // Composable for character details screen with characterId argument
                    composable(Screens.CharacterDetailsScreen.route + "/{characterId}", listOf(
                        navArgument("characterId") {
                            type = NavType.StringType
                        }
                    )) {
                        val characterId = it.arguments?.getString("characterId") ?: ""
                        CharacterDetailFragment(
                            characterId,
                            characterViewModel = detailsViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}