package com.rosafi.test.data.remote


import com.rosafi.test.data.model.Delivery
import com.rosafi.test.data.model.DoneRequestBody
import com.rosafi.test.data.model.ClientDoneResponse
import com.rosafi.test.data.model.VerificationRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {

    @GET("elements")
    suspend fun getDeliveries(): Response<ArrayList<Delivery>>


    @POST("elements/mark_as_done_by_sender")
    suspend fun confirmDeliveryByCarrier(@Body confirmationBody: DoneRequestBody): Response<ClientDoneResponse>

    @POST("elements/mark_as_done_by_receiver")
    suspend fun confirmDeliveryByClient(@Body confirmationBody: DoneRequestBody): Response<ClientDoneResponse>

    @POST("elements/verify-code")
    suspend fun verifyConfirmationCode(@Body verificationRequestBody: VerificationRequestBody): Response<ClientDoneResponse>

}
