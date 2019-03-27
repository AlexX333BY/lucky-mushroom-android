package by.bsuir.luckymushroom.nn.impl

import android.media.Image
import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork

import java.io.InputStream

class NeuralMushroomRecognizer(modelAndWeightsStream: InputStream) : MushroomRecognizer {
    override fun recognize(image: Image): RecognitionResult {
        TODO("recognizing")
    }

    protected val neuralNetwork: MultiLayerNetwork = KerasModelImport.importKerasSequentialModelAndWeights(modelAndWeightsStream);
}
