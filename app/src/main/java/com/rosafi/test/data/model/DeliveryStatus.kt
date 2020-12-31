package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class DeliveryStatus(
    @SerializedName("status")
    var status: String?
)