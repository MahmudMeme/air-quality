package com.example.airpolution.di

import android.app.Application
import android.content.Context
import com.example.airpolution.data.local.LocalDataSource
import com.example.airpolution.data.local.LocalDataSourceImpl
import com.example.airpolution.data.remote.AirValuesDBApi
import com.example.airpolution.data.remote.RemoteAirValuesDataSource
import com.example.airpolution.data.remote.RemoteAirValuesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModule {

    companion object {
        @Provides
        fun provideContext(app: Application): Context {
            return app
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            val baseUrl = "https://placeholder.pulse.eco/"
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @Provides
        @Singleton
        fun provideAirValuesDBApi(retrofit: Retrofit): AirValuesDBApi {
            return retrofit.create(AirValuesDBApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindRemoteAirValuesDataSource(impl: RemoteAirValuesDataSourceImpl): RemoteAirValuesDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource
}