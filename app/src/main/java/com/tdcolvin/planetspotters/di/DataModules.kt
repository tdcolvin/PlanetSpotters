package com.tdcolvin.planetspotters.di

import android.content.Context
import androidx.room.Room
import com.tdcolvin.planetspotters.data.repository.DefaultPlanetsRepository
import com.tdcolvin.planetspotters.data.repository.PlanetsRepository
import com.tdcolvin.planetspotters.data.source.remote.ApiRemoteDataSource
import com.tdcolvin.planetspotters.data.source.local.LocalDataSource
import com.tdcolvin.planetspotters.data.source.local.PlanetsDatabase
import com.tdcolvin.planetspotters.data.source.local.RoomLocalDataSource
import com.tdcolvin.planetspotters.data.source.remote.RemoteDataSource
import com.tdcolvin.planetspotters.domain.AddPlanetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return ApiRemoteDataSource()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        database: PlanetsDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): LocalDataSource {
        return RoomLocalDataSource(database.planetsDao(), ioDispatcher)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PlanetsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PlanetsDatabase::class.java,
            "Planets.db"
        ).build()
    }
}
