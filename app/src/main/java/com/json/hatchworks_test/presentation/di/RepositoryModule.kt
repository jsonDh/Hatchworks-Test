package com.json.hatchworks_test.presentation.di

import com.apollographql.apollo3.ApolloClient
import com.json.hatchworks_test.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Dagger Hilt module for providing repository-related dependencies.
 */
@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    /**
     * Provides an instance of [ApolloClient] scoped to ViewModels.
     *
     * This function creates and configures an [ApolloClient] instance with the server URL
     * defined in [BuildConfig.APOLLO_API].
     *
     * @return An instance of [ApolloClient].
     */
    @Provides
    @ViewModelScoped
    fun providesApollo() : ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.APOLLO_API)
            .build()
    }
}