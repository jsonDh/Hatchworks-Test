# Screens

## Overview
`Screens` is a sealed class representing different screens in the app along with their corresponding routes. It provides static objects for each screen to ensure type safety when navigating between screens.

## Objects
- **CharacterListScreen**: Represents the character list screen.
- **CharacterDetailsScreen**: Represents the character details screen.

## Functions
- **withArgs**: Generates a route string with optional arguments. It appends arguments to the route separated by slashes.

### Example Usage
```kotlin
val characterDetailsRoute = Screens.CharacterDetailsScreen.withArgs("characterId")
// Output: "characters_details_screen/characterId"
```

### Summary
`Screens` ensures clarity and type safety when defining and navigating between different screens in the app. It encapsulates screen routes and facilitates navigation with optional arguments.