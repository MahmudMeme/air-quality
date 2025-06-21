package com.example.airpolution.data.remote

import javax.inject.Inject

class RemoteAirValuesDataSourceImpl @Inject constructor(
    private val airValuesDBApi: AirValuesDBApi,
) : RemoteAirValuesDataSource {
    override suspend fun getAirValues(url: String): AirQualityResponse {

        val airValuesResponse = airValuesDBApi.getAirValues(url)
        val bodyResponse = airValuesResponse.body()
        if (airValuesResponse.isSuccessful && bodyResponse != null) {
            return AirQualityResponse(bodyResponse.cityName, bodyResponse.values)
        }
        throw Exception("padna vo retrofit air values data source")
    }

    override suspend fun getAverageDataForYesterday(url: String): List<AverageDataResponse> {
        val response = airValuesDBApi.getAverageDataForYesterday(url)
        val bodyResponse = response.body()
        if (response.isSuccessful && bodyResponse != null) {
            return bodyResponse
        }
        throw Exception("padna vo retrofit air values funkcija za podatoci od vcera")
    }
}