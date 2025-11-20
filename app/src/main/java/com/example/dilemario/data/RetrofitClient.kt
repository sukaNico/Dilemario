import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.dilemario.data.ApiService

object RetrofitClient {

    private const val BASE_URL = "https://nonstructured-machelle-preaexistent.ngrok-free.dev/"

    private var token: String? = null

    fun setToken(jwt: String) {
        token = jwt
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val requestBuilder = original.newBuilder()
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
