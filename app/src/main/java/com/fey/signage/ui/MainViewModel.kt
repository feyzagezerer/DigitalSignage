package com.fey.signage.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fey.signage.api.VideoPlayerRepository
import com.fey.signage.model.Params
import com.fey.signage.model.request.CommandStatusRequest
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: VideoPlayerRepository
): ViewModel() {

    private lateinit var uuid: String

    private var paramsList: List<Params>?  = null
    private  var command: Long = 0
    fun setup(uuid: String){
        this.uuid = uuid
        loadScreen()
        loadScreenAndCheckStatus()
        }


    private fun loadScreen(): Long{

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loadScreen(uuid)
            if(result.body() != null){
                val screenResponse = result.body()
                paramsList = screenResponse?.params
                paramsList?.forEach { params ->

                   command = params.sync?.commandID!!
                    params.sync?.data?.forEach {
                        Timber.tag("Check Data").e("Name : %s, Type : %s", it.name, it.type)
                    }
                }
            }
        }
        return command
    }
private fun loadScreenAndCheckStatus() {
    viewModelScope.launch(Dispatchers.IO) {
        val command = loadScreen()
        checkStatus(command.toString())
    }
}

    private fun checkStatus(command: String){
        viewModelScope.launch(Dispatchers.IO) {
            val mediaType = "application/json".toMediaType()
            val requestBody = Gson().toJson(CommandStatusRequest(command)).toRequestBody(mediaType)
            val result = repository.checkStatus(uuid, requestBody)
            if(result.body() != null){
                val checkStatusResponse = result.body()
                Timber.tag("Check Command Status").e("Status : %s", checkStatusResponse?.message?.status)
            }
        }
    }
}