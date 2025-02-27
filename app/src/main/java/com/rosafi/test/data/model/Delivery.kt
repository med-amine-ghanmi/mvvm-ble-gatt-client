package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class Delivery(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("receiver_uuid")
    val receiverUuid: String?,
    @SerializedName("sender_uuid")
    val senderUuid: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("uuid")
    val uuid: String?
){



}

