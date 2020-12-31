package com.rosafi.test.data.data_manager

import com.rosafi.test.data.model.*
import com.rosafi.test.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
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

    fun markDeliveryAsDoneByClient(deliveryUUID: String) {
        val confirmDeliveryByClient: Flow<ClientDoneResponse> = flow {
            val response = retroFitClient.getRetrofitClient().confirmDeliveryByClient(deliveryUUID)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                } ?: kotlin.run {
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    fun markDeliveryAsDoneByCarrier(deliveryUUID: String) : Flow<CarrierDoneResponse>{
       return flow {
            val response = retroFitClient.getRetrofitClient().confirmDeliveryByCarrier(DoneRequestBody(deliveryUUID))
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                }
            }
        }.flowOn(Dispatchers.IO)
    }


    fun verifyConfirmationCode(verificationRequestBody: VerificationRequestBody) {
        val confirmDeliveryByClient: Flow<DeliveryStatus> = flow {
            val response = retroFitClient.getRetrofitClient().verifyConfirmationCode(verificationRequestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(it)
                } ?: kotlin.run {
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}