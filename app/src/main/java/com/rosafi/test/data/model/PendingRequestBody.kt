package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class PendingRequestBody(
    @SerializedName("element_uuid")
    var deliveryUuid: DeliveryStatus?
)