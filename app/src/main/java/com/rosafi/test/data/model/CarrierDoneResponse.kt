package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class CarrierDoneResponse(
    @SerializedName("status")
    val status: String?
)