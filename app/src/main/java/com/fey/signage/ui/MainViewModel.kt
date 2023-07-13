package com.fey.signage.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fey.signage.GenerateUUID
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
    private val _command = MutableLiveData<Long>()
    val command: LiveData<Long> get() = _command

    private val _commandSync = MutableLiveData<Long?>()
    val commandSync: LiveData<Long?> get() = _commandSync
    private var paramsList: List<Params>?  = null

    fun setup(uuid: MutableLiveData<String>){
        this.uuid = uuid.toString()
        Timber.tag("Check Uuid").e("UUID ${GenerateUUID.uuid.value}")
        forReportCommand()
        loadScreenAndCheckStatus()
        forSyncCommand()
        }


    private fun forReportCommand(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.loadScreen(uuid)
            if(result.body() != null){
                val screenResponse = result.body()
                paramsList = screenResponse?.params
                paramsList?.forEach{params ->
                    _command.value = params.report?.commandID
                }
            }
        }
    }
    private fun forSyncCommand() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val result = repository.loadScreen(uuid)
                if (result.body() != null) {
                    val screenResponse = result.body()
                    paramsList = screenResponse?.params
                    paramsList?.forEach { params ->
                        _commandSync.value = params.sync?.commandIDSync
                    }
                }
            }
        } catch (exception: Exception) {

        }
    }
private fun loadScreenAndCheckStatus() {
    viewModelScope.launch(Dispatchers.IO) {
        val command = forReportCommand()
        checkStatus(command.toString())
        val commandSync = forSyncCommand()
        checkStatus(commandSync.toString())
    }
}

    private fun checkStatus(commandID: String){
        viewModelScope.launch(Dispatchers.IO) {
            val mediaType = "application/json".toMediaType()
            val requestBody = Gson().toJson(CommandStatusRequest(commandID)).toRequestBody(mediaType)
            val result = repository.checkStatus(uuid, requestBody)
            if(result.body() != null){
                val checkStatusResponse = result.body()
                Timber.tag("Check Command Status").e("Status : %s", checkStatusResponse?.message?.status)
            }
        }
    }
}