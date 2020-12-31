package com.rosafi.test.data.remote


import com.rosafi.test.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface API {

    @GET("elements")
    suspend fun getDeliveries(): Response<ArrayList<Delivery>>


    @Headers("Content-Type: application/json")
    @POST("elements/mark_as_done_by_sender")
    suspend fun confirmDeliveryByCarrier(@Body doneRequestBody: DoneRequestBody): Response<CarrierDoneResponse>

    @Headers("Content-Type: application/json")
    @POST("elements/mark_as_done_by_receiver")
    suspend fun confirmDeliveryByClient(@Body doneRequestBody: DoneRequestBody): Response<ClientDoneResponse>

    @Headers("Content-Type: application/json")
    @POST("elements/verify-code")
    suspend fun verifyConfirmationCode(@Body verificationRequestBody: VerificationRequestBody): Response<DeliveryStatus>

}
