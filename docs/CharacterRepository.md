# Character Repository

The `CharacterRepository` interface defines methods for interacting with character-related data.

## Methods

### `suspend fun getCharacterList(): CharactersListQuery.Result`

Fetches a list of characters.

- Returns: A list of characters wrapped in a `CharactersListQuery.Result` object.

### `suspend fun getCharacterDetails(characterId: String): CharacterQuery.Data`

Fetches details of a specific character identified by the provided `characterId`.

- Parameters:
  - `characterId`: The ID of the character to fetch.
- Returns: Details of the character wrapped in a `CharacterQuery.Data` object.