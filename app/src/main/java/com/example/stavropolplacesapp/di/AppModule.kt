package com.example.stavropolplacesapp.di

import android.content.Context
import com.example.stavropolplacesapp.data.repository.PlacesRepository
import com.example.stavropolplacesapp.data.repository.PlacesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePlacesRepository(
        @ApplicationContext context: Context
    ): PlacesRepository = PlacesRepositoryImpl(context)
}
