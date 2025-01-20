package com.example.movie_and_tvshows_app.dependency_injection

import com.example.movie_and_tvshows_app.network.WatchmodeApiService
import com.example.movie_and_tvshows_app.utils.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class) // Ensures that this module is installed in the SingletonComponent scope
class AppModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        return ApiClient
    }

    @Provides
    @Singleton
    fun provideWatchmodeApiService(apiClient: ApiClient): WatchmodeApiService {
        return apiClient.createApiService() // Using ApiClient to create the WatchmodeApiService
    }
}
