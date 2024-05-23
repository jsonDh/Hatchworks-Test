package com.json.hatchworks_test.presentation.view

sealed class Screens(val route: String) {
    object CharacterListScreen : Screens("characters_list_screen")
    object CharacterDetailsScreen : Screens("characters_details_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}