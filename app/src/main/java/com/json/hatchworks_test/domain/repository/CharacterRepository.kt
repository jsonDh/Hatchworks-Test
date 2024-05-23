package com.json.hatchworks_test.domain.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.json.hatchworks_test.CharactersListQuery

interface CharacterRepository {

    suspend fun getCharacterList(): ApolloResponse<CharactersListQuery.Data>

}