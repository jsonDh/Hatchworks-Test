# CharacterListViewModel

## Overview
`CharacterListViewModel` is responsible for managing character list-related operations and maintaining the state of the character list. It interacts with the repository to fetch data and exposes the state using a StateFlow.

## Functions
- **getCharacters**: Retrieves the list of characters.
- **getCharactersList**: Fetches the list of characters from the repository.

## Properties
- **charactersListState**: StateFlow representing the state of the characters list.

## Companion Object
- **TAG**: Tag used for logging purposes.

## Sealed Class - CharactersListState
- Represents the various states of the characters list.
- **Initial**: Initial state indicating no characters have been loaded yet.
- **Loading**: State indicating characters are currently being loaded.
- **Success**: State indicating successful loading of characters with the provided data.
- **Error**: State indicating an error occurred while loading characters.

## Summary
`CharacterListViewModel` encapsulates character list-related functionality and provides a clear way to manage and observe the state of the character list in the application.