//package com.example.airpolution.data
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class AirValuesDBApiProvider {
//    companion object {
//        @Volatile //kreirame statictki singleton objekt od MovieDbApi
//        private var INSTANCE: AirValuesDBApi? = null
//
//        @JvmStatic //ovaa oznaka znaci deka ako koristime java kod ovoj metod ke bide staticki
//        fun getQuotesDbApi(): AirValuesDBApi {
//            // if the INSTANCE is not null, then return it,
//            // if it is, then create the database
//            return INSTANCE ?: synchronized(this) {
//                val instance = createQuoteDbApi()
//                INSTANCE = instance
//                // return instance
//                instance
//            }
//        }
//
//
//        private fun createQuoteDbApi(): AirValuesDBApi {
//            val BASE_URL = "https://skopje.pulse.eco/rest/overall"
//
//            return Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(AirValuesDBApi::class.java)
//        }
//    }
//}