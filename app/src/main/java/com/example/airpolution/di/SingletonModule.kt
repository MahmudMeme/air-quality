package com.example.airpolution.di

import com.example.airpolution.data.AirValuesDBApi
import com.example.airpolution.data.RemoteAirValuesDataSource
import com.example.airpolution.data.RemoteAirValuesDataSourceImpl
import com.example.airpolution.data.Repository
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

        @Provides
        @Singleton
        fun provideRemoteAirValuesDataSource(
            airValuesDBApi: AirValuesDBApi
        ): RemoteAirValuesDataSource {
            return RemoteAirValuesDataSourceImpl(airValuesDBApi)
        }

        @Provides
        @Singleton
        fun provideRepository(remoteAirValuesDataSource: RemoteAirValuesDataSource): Repository {
            return Repository(remoteAirValuesDataSource)
        }
    }
}