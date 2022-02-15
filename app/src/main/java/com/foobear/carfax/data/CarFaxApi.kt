package com.foobear.carfax.data

import com.foobear.carfax.data.models.CarDetailsListingsRequest
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface CarFaxApi {

    @GET(".")
    fun getCarDetailsListings(

    ): Flowable<CarDetailsListingsRequest>

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
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                    .create(CarFaxApi::class.java)
        }
    }
}