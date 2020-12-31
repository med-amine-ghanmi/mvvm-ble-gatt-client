package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class VerificationRequestBody(
    @SerializedName("element_uuid")
    var elementUuid: String?,
    @SerializedName("confirmation_code")
    var confirmationCode: String?
)