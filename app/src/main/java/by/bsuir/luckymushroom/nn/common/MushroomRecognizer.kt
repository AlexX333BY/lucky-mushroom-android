package by.bsuir.luckymushroom.nn.common

import android.media.Image;

interface MushroomRecognizer {
    fun initialize(dataPath: String)
    fun recognize(image: Image): RecognitionResult
}
