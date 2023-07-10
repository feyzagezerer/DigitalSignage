package com.fey.signage.model.request

import com.google.gson.annotations.SerializedName

data class CommandStatusRequest(
    @SerializedName("command_id")
    val commandId: String,
    @SerializedName("data")
    val data: Any? = null
)