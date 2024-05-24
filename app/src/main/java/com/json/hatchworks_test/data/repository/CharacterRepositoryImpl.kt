package com.json.hatchworks_test.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
import javax.inject.Inject

/**
 * Implementation of the [CharacterRepository] interface.
 * Handles fetching character data using ApolloClient.
 */
class CharacterRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) : CharacterRepository {
    /**
     * Fetches a list of characters.
     *
     * @return ApolloResponse containing the characters list data.
     */
    override suspend fun getCharacterList(): ApolloResponse<CharactersListQuery.Data> =
        apolloClient.query(CharactersListQuery()).execute()

    /**
     * Fetches details of a specific character.
     *
     * @param id The ID of the character to fetch.
     * @return ApolloResponse containing the character details data.
     */
    override suspend fun getCharacterDetails(id: String): ApolloResponse<CharacterQuery.Data> =
        apolloClient.query(CharacterQuery(id)).execute()
}