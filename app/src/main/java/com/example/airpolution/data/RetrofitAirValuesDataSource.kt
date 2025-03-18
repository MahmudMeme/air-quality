package com.example.airpolution.data

class RetrofitAirValuesDataSource (private val airValuesDBApi: AirValuesDBApi) : RemoteAirValuesDataSource {
    override suspend fun getAirValues(): AirQualityResponse {
        val response = airValuesDBApi.getAirValues()
        val bodyResponse = response.body()
        if (response.isSuccessful && bodyResponse!=null){
            return AirQualityResponse(bodyResponse.cityName,bodyResponse.values)
        }
        throw Exception("padna vo retrofit air values data source")
    }
}