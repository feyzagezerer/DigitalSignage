package com.fey.signage.model.response

import com.fey.signage.model.Message
import com.google.gson.annotations.SerializedName

data class CommandStatusResponse(
    @SerializedName("success" )
    val success : Boolean,
    @SerializedName("message" )
    val message : Message
)