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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: VideoPlayerRepository
) : ViewModel() {

    private var uuid: String? = null
    private val _command = MutableLiveData<Long?>()
    val command: LiveData<Long?> get() = _command
    private var paramsList: List<Params>? = null

    fun setup(uuid: MutableLiveData<String>) {
        this.uuid = uuid.value
        Timber.tag("Check UUID").e("UUID ${uuid.value}")
        loadScreenAndCheckStatus()
    }


    private fun forReportCommand() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val result = repository.loadScreen(uuid)
                if (result.body() != null) {
                    val screenResponse = result.body()
                    paramsList = screenResponse?.params
                    paramsList?.forEach { params ->
                        if (params.sync?.commandIDSync == null) {
                            _command.postValue(params.report?.commandID)
                            checkStatus(_command.value.toString())
                        } else {
                            _command.postValue(params.sync.commandIDSync)
                            checkStatus(_command.value.toString())
                        }
                    }
                    Timber.tag("Check CommandID").e("command id : %s", _command.value)
                }

                delay(10000)
            }
        }
    }

    private fun loadScreenAndCheckStatus() {
        forReportCommand()


    }

    private fun checkStatus(commandID: String) {

        viewModelScope.launch(Dispatchers.IO) {
            Timber.tag("Check commandID").e("commandID : %s", commandID)
            val mediaType = "application/json".toMediaType()
            val requestBody =
                Gson().toJson(CommandStatusRequest(commandID)).toRequestBody(mediaType)
            val result = repository.checkStatus(uuid, requestBody)
            if (result.body() != null) {
                val checkStatusResponse = result.body()
                Timber.tag("Check Command Status")
                    .e("Status : %s", checkStatusResponse?.message?.status)
            }
        }
    }
}