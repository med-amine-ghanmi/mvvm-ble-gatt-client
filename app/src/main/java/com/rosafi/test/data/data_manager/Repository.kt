package com.rosafi.test.data.data_manager

import com.rosafi.test.data.model.Delivery
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



}