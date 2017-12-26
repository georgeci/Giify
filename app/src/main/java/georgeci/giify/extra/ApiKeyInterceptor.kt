package georgeci.giify.extra

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
        private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()
        val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()

        val requestBuilder = originalRequest.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}