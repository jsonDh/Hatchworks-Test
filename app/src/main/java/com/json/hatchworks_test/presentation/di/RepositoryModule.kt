package com.json.hatchworks_test.presentation.di

import com.apollographql.apollo3.ApolloClient
import com.json.hatchworks_test.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesApollo() : ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.APOLLO_API)
            .build()
    }
}