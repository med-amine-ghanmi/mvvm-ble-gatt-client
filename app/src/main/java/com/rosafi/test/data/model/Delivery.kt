package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class Delivery(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("receiver_uuid")
    var receiverUuid: String?,
    @SerializedName("sender_uuid")
    var senderUuid: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("uuid")
    var uuid: String?
){


    enum class DeliveryStatuses{
        PENDING, DONE, DONE_BY_SENDER, DONE_BY_RECEIVER
    }


    enum class DeliveryStatusesToShow{
        PENDING, DONE, DONE_BY_CARRIER, DONE_BY_CLIENT
    }

}
