package com.json.hatchworks_test.presentation.view

/**
 * Sealed class representing different screens in the app with their corresponding routes.
 */
sealed class Screens(val route: String) {
    /**
     * Object representing the character list screen.
     */
    data object CharacterListScreen : Screens("characters_list_screen")
    /**
     * Object representing the character details screen.
     */
    data object CharacterDetailsScreen : Screens("characters_details_screen")

    /**
     * Generates a route string with optional arguments.
     *
     * @param args Variable number of arguments to append to the route.
     * @return The complete route string with appended arguments.
     */
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}