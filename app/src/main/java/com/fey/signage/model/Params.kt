package com.fey.signage.model

import com.fey.signage.model.Report
import com.fey.signage.model.Sync
import com.google.gson.annotations.SerializedName

data class Params (
    @SerializedName("report")
    val report: Report? = null,
    @SerializedName("sync")
    val sync: Sync? = null
)







