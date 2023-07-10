package com.fey.signage.api

import com.fey.signage.model.response.ScreenResponse
import com.fey.signage.model.response.CommandStatusResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface OctopusApi {
    @GET("screen/{uuid}")
    suspend fun loadScreen(@Path("uuid") uuid: String): Response<ScreenResponse>


    @Headers("Content-Type: application/json")
    @POST("screen/{uuid}")
    suspend fun checkCommandStatus(
        @Path("uuid") uuid: String,
        @Body requestBody: RequestBody
    ): Response<CommandStatusResponse>
}