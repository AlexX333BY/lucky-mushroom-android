package by.bsuir.luckymushroom.app.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.res.AssetManager
import android.net.Uri
import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import by.bsuir.luckymushroom.nn.impl.NeuralMushroomRecognizer
import kotlinx.coroutines.*
import org.apache.commons.io.FileUtils
import java.io.File

class AppViewModel : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var photoURI: Uri? = null
    var recognizer: MushroomRecognizer? = null
//    var recognitionResult: Array<RecognitionResult>? = null

    private val isRecognition: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = false
        }
    }

    private val recognitionResult = MutableLiveData<RecognitionResult?>()

    private val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().also {
            it.value = false
        }
    }

    fun setIsRecognition(value: Boolean) {
        isRecognition.postValue(value)
    }

    fun getIsRecognition(): LiveData<Boolean> {
        return isRecognition
    }

    fun getRecognitionResult(): LiveData<RecognitionResult?> {
        return recognitionResult
    }

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun launchRecognizerInit(assets: AssetManager) {
        uiScope.launch {
            isLoading.postValue(true)
            initRecognizer(assets)
            isLoading.postValue(false)
        }
    }

    fun launchRecognize() {
        uiScope.launch {
            isLoading.postValue(true)
            recognize()
            isRecognition.postValue(false)
            isLoading.postValue(false)
        }
    }

    suspend fun initRecognizer(assets: AssetManager) =
        withContext(Dispatchers.Default) {

            val modelFile = File.createTempFile("model_with_weights", ".tmp")
            modelFile.deleteOnExit()

            FileUtils.copyInputStreamToFile(
                assets.open("model_with_weights.pb"), modelFile
            )

            val labelsFile = File.createTempFile("labels", ".tmp")
            labelsFile.deleteOnExit()

            FileUtils.copyInputStreamToFile(
                assets.open("labels.txt"), labelsFile
            )

            recognizer = NeuralMushroomRecognizer(modelFile, labelsFile)
        }

    suspend fun recognize() = withContext(Dispatchers.Default) {
        val rez = try {
            recognizer?.recognize(File(photoURI?.path))
        } catch (ex: Error) {
            null
        } catch (ex: Exception) {
            null
        }

        recognitionResult.postValue(
            rez?.reduce { a, b -> if (a.probability > b.probability) a else b }
                ?.takeIf { it.probability > 0.6 })
    }

}
