package com.example.unitodoapp.data.api.interceptors

import android.util.Log
import com.example.unitodoapp.utils.RETRY_COUNT
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import javax.inject.Singleton

@Singleton
class RetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var responseOK = false
        var tryCount = 0

        while (!responseOK && tryCount < RETRY_COUNT) {
            try {
                response = chain.proceed(request)
                responseOK = response.isSuccessful
            } catch (e: HttpException) {
                Log.d("intercept", "HTTP error occurred - $tryCount: ${e.code()}")
            } finally {
                tryCount++
            }
        }

        return response!!
    }
}