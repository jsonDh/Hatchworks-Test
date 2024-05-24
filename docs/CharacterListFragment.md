# CharacterListFragment

## Overview
`CharacterListFragment` is a composable function responsible for displaying a list of characters from the Rick & Morty series. It fetches character data using `CharacterListViewModel` and manages different UI states based on the data retrieval process.

## Key Components

### Parameters
- **charactersViewModel**: ViewModel used to fetch and manage the characters list.
- **navController**: NavController for managing navigation actions within the app.

### Sub-Composables
- **ShowCharactersList**: Displays the list of characters based on the current state.
- **CharacterCard**: Displays a card for each character in the list.
- **EmptyList**: Displays a message when the character list is empty or loading.

## Responsibilities
- **Data Fetching**: Initiates data fetching for the characters list using `CharacterListViewModel`.
- **Navigation Handling**: Navigates to the character details screen when a character card is clicked.
- **UI State Management**: Displays appropriate UI states (loading, success, error, empty) based on the state of the characters list.

## Summary
`CharacterListFragment` efficiently manages the display of character data in a grid layout. It ensures a smooth user experience by handling different UI states and providing clear feedback to the user.

### Additional Comments
- The use of `LaunchedEffect` ensures that data fetching is triggered when the composable is first displayed.
- The `CharacterCard` composable encapsulates the UI for displaying individual characters, enhancing modularity and maintainability.
- `EmptyList` provides a clear message to users when there are no characters to display or when the data is loading.