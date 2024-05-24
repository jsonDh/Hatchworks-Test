package com.json.hatchworks_test.presentation.di

import com.json.hatchworks_test.data.repository.CharacterRepositoryImpl
import com.json.hatchworks_test.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Dagger Hilt module for providing ViewModel-related dependencies.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    /**
     * Binds [CharacterRepositoryImpl] to [CharacterRepository].
     *
     * This allows instances of [CharacterRepositoryImpl] to be provided
     * whenever [CharacterRepository] is requested.
     *
     * @param repository The concrete implementation of [CharacterRepository].
     * @return An instance of [CharacterRepository].
     */
    @Binds
    @ViewModelScoped
    abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl) : CharacterRepository
}