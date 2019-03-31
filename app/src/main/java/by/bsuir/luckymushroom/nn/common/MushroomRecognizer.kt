package by.bsuir.luckymushroom.nn.common

import java.io.File

interface MushroomRecognizer {
    fun recognize(image: File): Array<RecognitionResult>
}
