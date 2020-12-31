package com.rosafi.test.ui.deliveries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosafi.test.data.data_manager.Repository
import com.rosafi.test.data.model.CarrierDoneResponse
import com.rosafi.test.data.model.ClientDoneResponse
import com.rosafi.test.data.model.Delivery
import com.rosafi.test.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.Collector
import kotlin.collections.ArrayList


class DeliveriesViewModel : ViewModel() {

    private val repository = Repository(RetrofitClient)

    //deliveries
    private  val _deliveriesLiveData = MutableLiveData<ArrayList<Delivery>>()
    val deliveriesLiveData: LiveData<ArrayList<Delivery>> = _deliveriesLiveData

    //DeliveryDoneAction
    private  val _markAsDoneLiveData = MutableLiveData<Pair<ClientDoneResponse,Int>>()
    val markAsDoneLiveData: LiveData<Pair<ClientDoneResponse,Int>> = _markAsDoneLiveData


    fun getRemoteDeliveries(){

        viewModelScope.launch {
            repository.getDeliveries.flowOn(Dispatchers.IO).collect {

                _deliveriesLiveData.postValue(it)

            }
        }

    }

   private fun markDeliveryAsDoneByClient(deliveryUUID: String, position: Int){

        viewModelScope.launch {
            repository.markDeliveryAsDoneByClient(deliveryUUID).flowOn(Dispatchers.IO).collect{
                _markAsDoneLiveData.postValue(Pair(it, position))
            }
        }
    }

    fun onMarkAsDoneButtonClicked(delivery: Delivery, position: Int){
        markDeliveryAsDoneByClient(delivery.uuid!!, position)
    }



}