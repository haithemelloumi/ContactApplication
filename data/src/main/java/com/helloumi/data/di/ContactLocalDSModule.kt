package com.helloumi.data.di

import com.helloumi.data.datasource.local.ContactLocalDS
import com.helloumi.data.datasource.local.ContactLocalDSImpl
import com.helloumi.data.datasource.local.dao.ContactDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ContactLocalDSModule {

    @Provides
    @Singleton
    fun provideContactLocalDS(
        dao: ContactDao
    ): ContactLocalDS = ContactLocalDSImpl(dao)
}
