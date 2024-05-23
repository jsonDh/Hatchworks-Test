package com.json.hatchworks_test.presentation.di

import com.json.hatchworks_test.data.repository.CharacterRepositoryImpl
import com.json.hatchworks_test.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl) : CharacterRepository

}