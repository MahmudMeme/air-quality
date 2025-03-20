package com.example.airpolution.di

import com.example.airpolution.data.AirValuesDBApi
import com.example.airpolution.data.RemoteAirValuesDataSource
import com.example.airpolution.data.Repository
import com.example.airpolution.data.RetrofitAirValuesDataSource
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
//    @Binds
//    abstract fun bindRemoteDataSource(impl: RetrofitAirValuesDataSource): RemoteAirValuesDataSource

    companion object {

        //        @Provides
//        @Singleton
//        fun provideAirValuesDBApi(): AirValuesDBApi {
//            return Retrofit.Builder().build().create(AirValuesDBApi::class.java)
//        }
//        @Provides
//        @Singleton
//        fun provideRetrofit(): Retrofit {
//            val BASE_URL = "https://skopje.pulse.eco/rest/overall"
//
//            return Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//        @Provides
//        @Singleton
//        fun provideAirValuesDBApi(retrofit: Retrofit): AirValuesDBApi {
//            return retrofit.create(AirValuesDBApi::class.java)
//        }
//        @Provides
//        @Singleton
//        fun provideRetrofitAirValuesDataSource(airValuesDBApi: AirValuesDBApi): RetrofitAirValuesDataSource {
//            return RetrofitAirValuesDataSource(airValuesDBApi)
//        }
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//            val BASE_URL = "https://skopje.pulse.eco/rest/overall/"
            val BASE_URL = "https://placeholder.pulse.eco/"
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            //.create(AirValuesDBApi::class.java)
        }

        @Provides
        @Singleton
        fun provideAirValuesDBApi(retrofit: Retrofit): AirValuesDBApi {
            return retrofit.create(AirValuesDBApi::class.java)
        }

        //        @Provides
//        @Singleton
//        fun provideRemoteAirValuesDataSource(airValuesDBApi: AirValuesDBApi): RemoteAirValuesDataSource {
//            return RetrofitAirValuesDataSource(airValuesDBApi)
//        }
        @Provides
        @Singleton
        fun provideRemoteAirValuesDataSource(airValuesDBApi: AirValuesDBApi,okHttpClient: OkHttpClient): RemoteAirValuesDataSource {
            return RetrofitAirValuesDataSource(airValuesDBApi,okHttpClient)
        }

        @Provides
        @Singleton
        fun provideRepository(remoteAirValuesDataSource: RemoteAirValuesDataSource): Repository {
            return Repository(remoteAirValuesDataSource)
        }
    }
}