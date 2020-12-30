package com.rosafi.test.data.remote


import com.rosafi.test.data.model.Delivery
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("elements")
    suspend fun getDeliveries(): Response<ArrayList<Delivery>>

}
