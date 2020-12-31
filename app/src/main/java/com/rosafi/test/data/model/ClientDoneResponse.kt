package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class ClientDoneResponse(
    @SerializedName("status")
    var status: String?,
    @SerializedName("confirmation_code")
    var confirmationCode: String?
)