package com.example.airpolution.data

import okhttp3.Interceptor
import okhttp3.Response

class DynamicBaseUrlInterceptor(private val city: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .host("$city.pulse.eco") // Dynamically set the subdomain
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}