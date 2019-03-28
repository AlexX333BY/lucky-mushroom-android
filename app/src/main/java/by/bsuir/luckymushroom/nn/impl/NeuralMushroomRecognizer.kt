package by.bsuir.luckymushroom.nn.impl

import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import java.io.File
import org.tensorflow.lite.Interpreter

class NeuralMushroomRecognizer(modelAndWeights: File) : MushroomRecognizer {
    override fun recognize(image: File): Array<RecognitionResult> {
        TODO("parse output")
    }

    protected val CHANNELS = 1;
    protected val neuralNetwork: Interpreter = Interpreter(modelAndWeights)
}
