package com.example.unitodoapp.di

import com.example.unitodoapp.data.api.ApiService
import com.example.unitodoapp.data.api.interceptors.RetryInterceptor
import com.example.unitodoapp.utils.CONNECT_TIMEOUT
import com.example.unitodoapp.utils.READ_TIMEOUT
import com.example.unitodoapp.utils.WRITE_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
interface ApiServiceModule {
    companion object {
        @Singleton
        @Provides
        fun provideApiService(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory
        ): ApiService {
            return Retrofit.Builder()
                .baseUrl("http://26.36.121.166:3000/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(ApiService::class.java)
        }
        @Singleton
        @Provides
        fun provideGsonConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }
        @Singleton
        @Provides
        fun provideOkHttpClient(
            retryInterceptor: RetryInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(retryInterceptor)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build()
        }

        @Singleton
        @Provides
        fun provideRetryInterceptor(): RetryInterceptor {
            return RetryInterceptor()
        }
    }
}