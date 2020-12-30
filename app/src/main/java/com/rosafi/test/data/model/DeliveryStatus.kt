package com.rosafi.test.data.model


import com.google.gson.annotations.SerializedName

data class ElementStatus(
    @SerializedName("status")
    val status: String?
)