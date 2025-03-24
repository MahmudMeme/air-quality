package com.example.airpolution.data

class RemoteAirValuesDataSourceImpl(
    private val airValuesDBApi: AirValuesDBApi
) :
    RemoteAirValuesDataSource {
    override suspend fun getAirValues(url: String): AirQualityResponse {

        val airValuesResponse = airValuesDBApi.getAirValues(url)
        val bodyResponse = airValuesResponse.body()
        if (airValuesResponse.isSuccessful && bodyResponse != null) {
            return AirQualityResponse(bodyResponse.cityName, bodyResponse.values)
        }
        throw Exception("padna vo retrofit air values data source")
    }
}