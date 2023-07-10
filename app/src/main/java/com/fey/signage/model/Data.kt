package com.fey.signage.model

import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("nth")
    val nth: Long,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("start_date")
    val startDate: Any? = null,
    @SerializedName("end_date")
    val endDate: Any? = null
)