package com.walksafe.app_titulacion.di

import com.walksafe.app_titulacion.data.IAppDataSource
import com.walksafe.app_titulacion.data.http.datasource.AppDataSource
import com.walksafe.app_titulacion.domain.IAppDataRepository
import com.walksafe.app_titulacion.domain.repository.AppDataRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

abstract class NetworkModule {

    @Binds
    abstract fun bindAppDataRepository(authDataRepository: AppDataRepository): IAppDataRepository

    @Binds
    abstract fun bindAppDataSource(authDataSource:AppDataSource): IAppDataSource

}