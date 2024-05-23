package com.json.hatchworks_test.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) : CharacterRepository {
    override suspend fun getCharacterList(): ApolloResponse<CharactersListQuery.Data> =
        apolloClient.query(CharactersListQuery()).execute()

    override suspend fun getCharacterDetails(id: String): ApolloResponse<CharacterQuery.Data> =
        apolloClient.query(CharacterQuery(id)).execute()
}