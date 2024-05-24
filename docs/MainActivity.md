# MainActivity

## Overview
`MainActivity` is the entry point of the Rick & Morty Character Viewer application. It is responsible for setting up the application's navigation and applying the theme.

## Key Components

### ViewModels
- **CharacterViewModel**: Manages the state and data related to individual character details.
- **CharacterListViewModel**: Manages the state and data related to the list of characters.

### NavController
`NavController` is used to manage navigation between the list and detail screens. It allows users to navigate from the character list to a character's details and back.

## Navigation
Navigation is handled using Jetpack Compose's `NavHost` and `composable` functions. The `NavHost` is set up with two main routes:
- **CharacterListScreen**: The starting destination that displays the list of characters.
- **CharacterDetailsScreen/{characterId}**: A screen that shows details for a selected character, identified by `characterId`.

## Themes
The application supports both day and night themes, applied through a custom theme called `HatchworksTestTheme`.

## Responsibilities
- **Setting Up Navigation**: `MainActivity` initializes the `NavController` and defines the navigation graph with the `NavHost`.
- **Applying Theme**: Ensures the app's theme is consistent across different screens.
- **ViewModel Initialization**: Initializes `CharacterViewModel` and `CharacterListViewModel` using the `by viewModels()` delegate provided by Hilt.

## Summary
`MainActivity` serves as the central hub for navigation and theme application in the Rick & Morty Character Viewer application. By using Jetpack Compose and Hilt, it manages the seamless transition between the character list and detail screens while maintaining a consistent look and feel.