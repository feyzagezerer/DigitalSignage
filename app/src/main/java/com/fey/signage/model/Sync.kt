package com.fey.signage.model

import com.google.gson.annotations.SerializedName

data class Sync (
    @SerializedName("command_id")
    val commandIDSync: Long?,
    @SerializedName("data")
    val data: List<Data>?
)