package com.example.airpolution.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitAirValuesDataSource(
    private val airValuesDBApi: AirValuesDBApi,
    private val okHttpClient: OkHttpClient
) :
    RemoteAirValuesDataSource {
    override suspend fun getAirValues(city: String): AirQualityResponse {

        val client = okHttpClient.newBuilder()
            .addInterceptor(DynamicBaseUrlInterceptor(city))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://placeholder.pulse.eco/") // Placeholder, will be replaced
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(AirValuesDBApi::class.java)

        val response = api.getAirValues()
        val bodyResponse = response.body()
        if (response.isSuccessful && bodyResponse != null) {
            return AirQualityResponse(bodyResponse.cityName, bodyResponse.values)
        }
        throw Exception("padna vo retrofit air values data source")
    }
}