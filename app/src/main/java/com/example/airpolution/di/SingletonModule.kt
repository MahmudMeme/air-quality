package com.example.airpolution.di

import com.example.airpolution.data.AirValuesDBApi
import com.example.airpolution.data.RemoteAirValuesDataSource
import com.example.airpolution.data.RetrofitAirValuesDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModule {
//    @Binds
//    abstract fun bindRemoteDataSource(impl: RetrofitAirValuesDataSource): RemoteAirValuesDataSource
@Provides
@Singleton
fun provideAirValuesDBApi(retrofit: Retrofit): AirValuesDBApi {
    return retrofit.create(AirValuesDBApi::class.java)
}

    @Provides
    @Singleton
    fun provideRetrofitAirValuesDataSource(airValuesDBApi: AirValuesDBApi): RetrofitAirValuesDataSource {
        return RetrofitAirValuesDataSource(airValuesDBApi)
    }
}