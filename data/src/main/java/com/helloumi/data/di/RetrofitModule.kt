package com.helloumi.data.di

import com.helloumi.data.datasource.remote.api.RandomUserAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.helloumi.data.di.RepositoryModule.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideRandomUserAPI(retrofit: Retrofit): RandomUserAPI {
        return retrofit.create(RandomUserAPI::class.java)
    }
}
