# CharacterDetailFragment

## Overview
`CharacterDetailFragment` is a composable function that displays detailed information about a specific character from the Rick & Morty series. It utilizes `CharacterViewModel` to fetch and manage character data and handles different UI states based on the current state of the data.

## Key Components

### Parameters
- **characterId**: The ID of the character to display details for.
- **characterViewModel**: The ViewModel used to fetch and manage character data.
- **navController**: The NavController used to manage navigation actions.

### Sub-Composables
- **CharacterDetailStates**: Determines which UI to display based on the current state of character data (success, loading, or error).
- **ShowCharacterDetails**: Displays the character details in portrait orientation.
- **ShowCharacterDetailsLandscape**: Displays the character details in landscape orientation.
- **EmptyView**: Shows a placeholder view while the character details are loading in portrait orientation.
- **EmptyViewLandscape**: Shows a placeholder view while the character details are loading in landscape orientation.
- **DetailData**: Displays a specific detail about the character.

## Responsibilities
- **Data Fetching**: Initiates data fetching for character details using `CharacterViewModel`.
- **Navigation Handling**: Handles back navigation and clears data when navigating back.
- **Orientation Handling**: Adjusts the UI based on the screen orientation (portrait or landscape).
- **UI State Management**: Displays appropriate UI states based on the current state of character data.

## Summary
`CharacterDetailFragment` is responsible for displaying detailed information about a specific character from the Rick & Morty series. It adapts the UI based on the screen orientation and manages different states of data loading, ensuring a smooth user experience.

### Additional Comments
- The use of `LaunchedEffect` ensures that data is fetched and listened to when the composable is first displayed.
- `BackPressHandler` manages the back navigation and clears the character data appropriately.
- Sub-composables are used to organize the code and handle different parts of the UI, making the code modular and easier to maintain.