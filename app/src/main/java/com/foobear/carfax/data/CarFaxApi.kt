package com.foobear.carfax.data

import com.foobear.carfax.data.models.CarDetailsListingsRequest
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface CarFaxApi {

    @GET(".")
    suspend fun getCarDetailsListings(

    ): Response<CarDetailsListingsRequest>

    companion object{
        operator  fun invoke(

        ): CarFaxApi{
            val BASE_URL = "https://carfax-for-consumers.firebaseio.com/assignment.json/"

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CarFaxApi::class.java)
        }
    }
}