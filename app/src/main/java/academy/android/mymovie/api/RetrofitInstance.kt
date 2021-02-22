package academy.android.mymovie.api

import academy.android.mymovie.utils.Constants.API_KEY
import academy.android.mymovie.utils.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
object RetrofitInstance {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private val contentType = "application/json".toMediaType()

    private val client = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(ApiKeyInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val movieApi: MovieApi = retrofit.create()
}

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val modifiedUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY).build()
        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()
        return chain.proceed(modifiedRequest)
    }
}