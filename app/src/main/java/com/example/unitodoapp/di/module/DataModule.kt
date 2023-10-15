package com.example.unitodoapp.di.module

import android.content.Context
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.TodoItemsRepository
import com.example.unitodoapp.data.db.AppDatabase
import com.example.unitodoapp.data.db.RevisionDao
import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.di.scope.AppScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @AppScope
    @Binds
    fun provideRepository(repository: Repository): TodoItemsRepository

    companion object {
        @AppScope
        @Provides
        fun provideTodoItemDao(database: AppDatabase): TodoItemDao {
            return database.getTodoItemDao()
        }

        @AppScope
        @Provides
        fun provideRevisionDao(database: AppDatabase): RevisionDao {
            return database.getRevisionDao()
        }

        @AppScope
        @Provides
        fun provideDatabase(context: Context): AppDatabase {
            return AppDatabase.getDatabaseInstance(context)
        }
    }
}