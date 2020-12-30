package com.rosafi.test.data.data_manager

import com.rosafi.test.data.model.CarrierDoneResponse
import com.rosafi.test.data.model.Delivery
import com.rosafi.test.data.model.DoneRequestBody
import com.rosafi.test.data.model.ClientDoneResponse
import com.rosafi.test.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository (private val retroFitClient: RetrofitClient) {

        val getDeliveries : Flow<ArrayList<Delivery>> = flow {
            val response = retroFitClient.getRetrofitClient().getDeliveries()
            if(response.isSuccessful){
                response.body()?.let {
                    emit(it)
                } ?: kotlin.run {
                }
            }
        }.flowOn(Dispatchers.IO)

    fun markDeliveryAsDoneByClient(doneRequestBody: DoneRequestBody) {
        val confirmDeliveryByClient: Flow<ClientDoneResponse> = flow {
            val response = retroFitClient.getRetrofitClient().confirmDeliveryByClient(doneRequestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                } ?: kotlin.run {
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    fun markDeliveryAsDoneByCarrier(doneRequestBody: DoneRequestBody) {
        val confirmDeliveryByClient: Flow<CarrierDoneResponse> = flow {
            val response = retroFitClient.getRetrofitClient().confirmDeliveryByCarrier(doneRequestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                } ?: kotlin.run {
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    fun verifyConfirmationCode(verifyRequestBody: DoneRequestBody) {
        val confirmDeliveryByClient: Flow<ClientDoneResponse> = flow {
            val response = retroFitClient.getRetrofitClient().confirmDeliveryByClient(verifyRequestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                } ?: kotlin.run {
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}