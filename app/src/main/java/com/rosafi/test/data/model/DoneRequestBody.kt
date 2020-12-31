package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class DoneRequestBody(
    @SerializedName("element_uuid")
    var elementUuid: String?
)