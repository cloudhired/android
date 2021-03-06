package com.cloudhired.api

import com.cloudhired.model.ProfessionalProfile
import com.cloudhired.model.ProfessionalSummary
import com.cloudhired.model.UpdateError
import com.google.gson.JsonObject
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface CloudhiredApi {
    @GET("/api/users")
    suspend fun getProSum() : ProSumResponse

    @GET("/api/{idtype}/{id}")
    suspend fun getProfile(@Path("id") id: String, @Path("idtype") idtype: String) : ProfileResponse

    @Headers("Content-Type: application/json")
    @POST("/api/email/{id}")
    suspend fun updateProfile(@Path("id") id: String, @Body data: JsonObject) : UpdateResponse

    data class ProSumResponse(val results: List<ProfessionalSummary>)
    data class ProfileResponse(val data: ProfessionalProfile)
    data class UpdateResponse(val error: UpdateError)

    companion object {
        // Leave this as a simple, base URL.  That way, we can have many different
        // functions (above) that access different "paths" on this server
        // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-http-url/
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("cloudhired.com")
            .build()

        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): CloudhiredApi = create(url)
        private fun create(httpUrl: HttpUrl): CloudhiredApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CloudhiredApi::class.java)
        }
    }
}