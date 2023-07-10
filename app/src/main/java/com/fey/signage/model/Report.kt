package com.fey.signage.model

import com.google.gson.annotations.SerializedName

data class Report (
    @SerializedName("command_id")
    val commandID: Long,
)