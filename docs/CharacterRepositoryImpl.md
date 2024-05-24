# Character Repository Implementation

The `CharacterRepositoryImpl` class implements the `CharacterRepository` interface to handle fetching character data using ApolloClient.

## Methods

`suspend fun getCharacterList(): ApolloResponse<CharactersListQuery.Data>`

Fetches a list of characters.

- Returns: An `ApolloResponse` containing the characters list data.

`suspend fun getCharacterDetails(id: String): ApolloResponse<CharacterQuery.Data>`

Fetches details of a specific character identified by the provided `id`.

- Parameters:
  - `id`: The ID of the character to fetch.
- Returns: An `ApolloResponse` containing the character details data.
