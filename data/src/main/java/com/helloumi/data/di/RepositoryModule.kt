package com.helloumi.data.di

import com.helloumi.data.repository.ContactRepositoryImpl
import com.helloumi.domain.repository.ContactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindContactRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository

    companion object {
        const val BASE_URL: String = "https://randomuser.me/"
    }
}
