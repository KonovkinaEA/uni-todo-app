package com.example.unitodoapp.di

import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.TodoItemsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Singleton
    @Binds
    fun provideRepository(repository: Repository): TodoItemsRepository
}
