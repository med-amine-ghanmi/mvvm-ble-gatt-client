package com.rosafi.test.ui.deliveries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosafi.test.data.data_manager.Repository
import com.rosafi.test.data.model.*
import com.rosafi.test.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class DeliveriesViewModel : ViewModel() {

    private val repository = Repository(RetrofitClient)

    //deliveries
    private  val _deliveriesLiveData = MutableLiveData<ArrayList<Delivery>>()
    val deliveriesLiveData: LiveData<ArrayList<Delivery>> = _deliveriesLiveData

    //DeliveryDoneAction
    private  val _markAsDoneLiveData = MutableLiveData<Pair<Delivery, Int>>()
    val markAsDoneLiveData: LiveData<Pair<Delivery, Int>> = _markAsDoneLiveData

    //VerificationCodeLiveData
    private  val _codeVerificationLiveData = MutableLiveData<Pair<VerificationRequestBody,DeliveryStatus>>()
    val codeVerificationLiveData: LiveData<Pair<VerificationRequestBody, DeliveryStatus>> = _codeVerificationLiveData

    fun getRemoteDeliveries(){

        viewModelScope.launch {
            repository.getDeliveries.flowOn(Dispatchers.IO).collect {
                _deliveriesLiveData.postValue(it)
            }
        }

    }

   private fun markDeliveryAsDoneByCarrier(delivery: Delivery, position: Int){

        viewModelScope.launch {
            repository.markDeliveryAsDoneByCarrier(delivery.uuid!!).flowOn(Dispatchers.IO).collect{
                _markAsDoneLiveData.postValue(Pair(delivery, position))
            }
        }
    }

    fun onMarkAsDoneButtonClicked(delivery: Delivery, position: Int){
        markDeliveryAsDoneByCarrier(delivery, position)
    }

    fun sendVerificationCode(verificationRequestBody: VerificationRequestBody){

        viewModelScope.launch {
            repository.verifyConfirmationCode(verificationRequestBody).flowOn(Dispatchers.IO).collect{
                _codeVerificationLiveData.postValue(Pair(verificationRequestBody,it))
            }
        }
    }


}