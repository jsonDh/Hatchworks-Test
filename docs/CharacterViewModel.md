# CharacterViewModel

## Overview
`CharacterViewModel` is responsible for managing character-related operations and maintaining the state of a single character. It interacts with the repository to fetch character details and exposes the state using a StateFlow.

## Functions
- **clearData**: Clears the character state.
- **getCharacterDetails(characterId: String)**: Retrieves the details of a character by its ID.
- **listen**: Collects and logs the current state of the character.

## Properties
- **characterState**: StateFlow representing the state of the character.

## Companion Object
- **TAG**: Tag used for logging purposes.

## Sealed Class - CharacterState
- Represents the various states of a character.
- **Initial**: Initial state indicating no character details have been loaded yet.
- **Loading**: State indicating character details are currently being loaded.
- **Success**: State indicating successful loading of character details with the provided data.
- **Error**: State indicating an error occurred while loading character details.

## Summary
`CharacterViewModel` encapsulates character-related functionality and provides a clear way to manage and observe the state of a single character in the application.