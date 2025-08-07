package com.helloumi.ui.di

import android.content.Context
import com.helloumi.ui.utils.network.ConnectivityManagerNetworkMonitor
import com.helloumi.ui.utils.network.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkMonitorModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return ConnectivityManagerNetworkMonitor(context)
    }
}