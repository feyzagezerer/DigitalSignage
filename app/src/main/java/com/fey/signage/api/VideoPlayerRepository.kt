package com.fey.signage.api

import okhttp3.RequestBody
import javax.inject.Inject


class VideoPlayerRepository @Inject constructor(
    private val octopusApi: OctopusApi,
) {

    suspend fun loadScreen(uuid: String?) =
        octopusApi.loadScreen(uuid!!)

    suspend fun checkStatus(uuid: String?, requestBody: RequestBody) = octopusApi.checkCommandStatus(uuid!!, requestBody)

}