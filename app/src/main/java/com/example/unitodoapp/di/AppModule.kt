package com.example.unitodoapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.example.unitodoapp.data.Repository
import com.example.unitodoapp.data.TodoItemsRepository
import com.example.unitodoapp.data.db.AppDatabase
import com.example.unitodoapp.data.db.RevisionDao
import com.example.unitodoapp.data.db.TodoItemDao
import com.example.unitodoapp.data.workmanager.CustomWorkManager
import com.example.unitodoapp.notifications.DeadlineNotificationService
import com.example.unitodoapp.notifications.TodoAlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Singleton
    @Binds
    fun provideRepository(repository: TodoItemsRepository): Repository

    companion object {

        @Singleton
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences("TABLE", Context.MODE_PRIVATE)
        }

        @Singleton
        @Provides
        fun provideNotificationService(
            @ApplicationContext context: Context
        ): DeadlineNotificationService {
            return DeadlineNotificationService(context)
        }

        @Singleton
        @Provides
        fun provideAlarmScheduler(
            @ApplicationContext context: Context
        ): TodoAlarmScheduler {
            return TodoAlarmScheduler(context)
        }

        @Singleton
        @Provides
        fun provideTodoItemDao(database: AppDatabase): TodoItemDao {
            return database.getTodoItemDao()
        }

        @Singleton
        @Provides
        fun provideRevisionDao(database: AppDatabase): RevisionDao {
            return database.getRevisionDao()
        }

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return AppDatabase.getDatabaseInstance(context)
        }

        @Singleton
        @Provides
        fun provideCustomWorkManager(workManager: WorkManager): CustomWorkManager {
            return CustomWorkManager(workManager)
        }

        @Singleton
        @Provides
        fun provideWorkManagerInstance(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}

