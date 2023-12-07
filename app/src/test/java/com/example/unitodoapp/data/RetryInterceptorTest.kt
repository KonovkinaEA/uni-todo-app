package com.example.unitodoapp.data

import com.example.unitodoapp.data.api.interceptors.RetryInterceptor
import com.example.unitodoapp.utils.RETRY_COUNT
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.fest.assertions.api.Assertions
import org.junit.Test
import retrofit2.HttpException

class RetryInterceptorTest {

    private val mockRequest = mockk<Request>()
    private val mockResponse = mockk<Response> {
        every { isSuccessful } returns true
    }
    private val mockChain = mockk<Interceptor.Chain> {
        every { request() } returns mockRequest
        every { proceed(mockRequest) } throws HttpException(
            retrofit2.Response.error<Any>(400, "Fake error".toResponseBody())
        )
        every { proceed(mockRequest) } returns mockResponse
    }

    private val retryInterceptor = RetryInterceptor()

    @Test
    fun testIntercept() {
        val result = retryInterceptor.intercept(mockChain)

        Assertions.assertThat(result.isSuccessful).isTrue
        verify(exactly = RETRY_COUNT) { mockChain.proceed(mockRequest) }
    }
}
