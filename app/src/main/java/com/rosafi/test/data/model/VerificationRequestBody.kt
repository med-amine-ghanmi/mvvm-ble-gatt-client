package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class VerificationRequestBody(
    @SerializedName("element_uuid")
    val elementUuid: String?,
    @SerializedName("confirmation_code")
    val confirmationCode: String?
)