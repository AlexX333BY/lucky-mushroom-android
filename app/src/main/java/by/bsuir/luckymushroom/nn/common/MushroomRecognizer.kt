package by.bsuir.luckymushroom.nn.common

import android.media.Image;

interface MushroomRecognizer {
    fun recognize(image: Image): Array<RecognitionResult>
}
