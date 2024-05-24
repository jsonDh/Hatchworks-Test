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

    private val detailsViewModel: CharacterViewModel by viewModels()
    private val listViewModel: CharacterListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HatchworksTestTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.CharacterListScreen.route
                ) {
                    composable(Screens.CharacterListScreen.route) {
                        CharacterListFragment(
                            charactersViewModel = listViewModel,
                            navController = navController
                        )
                    }
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