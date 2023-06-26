package com.tdcolvin.planetspotters.di

import com.tdcolvin.planetspotters.data.repository.DefaultPlanetsRepository
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import com.tdcolvin.planetspotters.data.source.local.LocalDataSource
import com.tdcolvin.planetspotters.data.source.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideAddPlanetUseCase(
        repository: PlanetsRepository
    ): AddPlanetUseCase {
        return AddPlanetUseCase(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePlanetsRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): PlanetsRepository {
        return DefaultPlanetsRepository(localDataSource, remoteDataSource, ioDispatcher)
    }
}