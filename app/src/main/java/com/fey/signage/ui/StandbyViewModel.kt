package com.fey.signage.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fey.signage.api.VideoPlayerRepository
import com.fey.signage.model.Data
import com.fey.signage.model.Params
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class StandbyViewModel @Inject constructor(
    private val repository: VideoPlayerRepository
) : ViewModel() {
     var uuid: String? = null

    private var paramsList: List<Params>? = null


    private var _screenCreated = MutableLiveData<Boolean>()
    var screenCreated : LiveData<Boolean> = _screenCreated

    fun setup(uuid: MutableLiveData<String>) {
        this.uuid = uuid.value
        Timber.tag("Check UUID").e("UUID ${uuid.value}")
        checkScreen()
    }


    private fun checkScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val result = repository.loadScreen(uuid)
                if (result.body() != null) {

                    val screenResponse = result.body()
                    paramsList = screenResponse?.params
                    if(paramsList!=null){
                        _screenCreated.postValue ( true)
                        break
                    }

                }

            }
        }

    }

fun screenCreated(): Boolean{

    if(_screenCreated.value == true){
        return true
    }
    else{
    return false}
}
}