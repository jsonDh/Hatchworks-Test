package com.json.hatchworks_test.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.CharactersListQuery

/**
 * Repository interface for interacting with character-related data.
 */
interface CharacterRepository {
    /**
     * Fetches a list of characters.
     *
     * @return A list of characters wrapped in a result object.
     */
    suspend fun getCharacterList(): ApolloResponse<CharactersListQuery.Data>
    /**
     * Fetches details of a specific character.
     *
     * @param characterId The ID of the character to fetch.
     * @return Details of the character wrapped in a result object.
     */
    suspend fun getCharacterDetails(id: String) : ApolloResponse<CharacterQuery.Data>
}