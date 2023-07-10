package com.fey.signage.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("screen_id")
    val screenId: Int? = null,
    @SerializedName("command")
    val command: String? = null,
    @SerializedName("data")
    val data: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)