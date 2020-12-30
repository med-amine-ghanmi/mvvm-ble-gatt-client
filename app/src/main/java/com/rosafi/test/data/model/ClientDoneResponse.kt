package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class ClientDoneResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("confirmation_code")
    val confirmationCode: String?
)