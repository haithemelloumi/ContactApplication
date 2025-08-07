package com.helloumi.data.di

import com.helloumi.data.datasource.remote.RandomUserRemoteDS
import com.helloumi.data.datasource.remote.RandomUserRemoteDSImpl
import com.helloumi.data.datasource.remote.api.RandomUserAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RandomUserRemoteDSModule {

    @Provides
    @Singleton
    fun provideRandomUserRemoteDS(
        randomUserAPI: RandomUserAPI
    ): RandomUserRemoteDS = RandomUserRemoteDSImpl(randomUserAPI)
}
