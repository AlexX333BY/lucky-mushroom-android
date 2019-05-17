package by.bsuir.luckymushroom.app.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.recognitionRequests.RecognitionRequest
import kotlinx.coroutines.*

class HistoryViewModel : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val recognitionRequests =
        MutableLiveData<Array<RecognitionRequest>>()

    fun getRecognitionRequests(): LiveData<Array<RecognitionRequest>> {
        return recognitionRequests
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun launchFetchData(appModel: AppViewModel) {
        uiScope.launch {
            appModel.setIsLoading(true)
            fetchData()
            appModel.setIsLoading(false)
        }
    }

    private suspend fun fetchData() = withContext(Dispatchers.IO) {
        try {
            App.cookie?.let {
                val response =
                    App.getRecognitionRequestService.getRecognitionRequests(it)
                        .await()
                if (response.isSuccessful) {
                    recognitionRequests.postValue(response.body())
                }
            }
        } catch (ex: Exception) {
        }
    }

}