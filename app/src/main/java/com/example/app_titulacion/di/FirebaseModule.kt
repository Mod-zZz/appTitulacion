package com.example.app_titulacion.di

import com.example.app_titulacion.data.IAuthDataSource
import com.example.app_titulacion.data.firebase.AuthDataSource
import com.example.app_titulacion.domain.IAuthDataRepository
import com.example.app_titulacion.domain.repository.AuthDataRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {

    @Binds
    abstract fun bindAuthDataRepository(authDataRepository: AuthDataRepository): IAuthDataRepository

    @Binds
    abstract fun bindAuthDataSource(authDataSource: AuthDataSource): IAuthDataSource
}