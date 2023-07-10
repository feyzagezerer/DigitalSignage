package com.fey.signage.api

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestTime = System.nanoTime()
        Timber.i("Request %s on %s%n%s", request.url, chain.connection(), request.headers)

        val response = chain.proceed(request)

        val responseTime = System.nanoTime()
        Timber.d(
            "Response for %s in %.1f ms%n%s",
            response.request.url,
            (responseTime - requestTime) / 1e6,
            response.headers
        )

        return response
    }


}