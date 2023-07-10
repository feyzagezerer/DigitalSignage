package com.fey.signage.model.response

import com.fey.signage.model.Params
import com.google.gson.annotations.SerializedName

data class ScreenResponse(
    @SerializedName("params")
    val params: List<Params>
)