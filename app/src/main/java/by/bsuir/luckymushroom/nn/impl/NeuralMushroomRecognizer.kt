package by.bsuir.luckymushroom.nn.impl

import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import java.io.File
import java.io.InputStream
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler
import org.datavec.image.loader.NativeImageLoader
import android.graphics.BitmapFactory

class NeuralMushroomRecognizer(modelAndWeightsStream: InputStream) : MushroomRecognizer {
    override fun recognize(image: File): Array<RecognitionResult> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(image.absolutePath, options)
        val height = options.outHeight
        val width = options.outWidth

        val loader = NativeImageLoader(height, width, CHANNELS)
        val imageMatrix = loader.asMatrix(image)
        val scalar = ImagePreProcessingScaler(0.0, 1.0);
        scalar.transform(imageMatrix)

        val output = neuralNetwork.output(imageMatrix, false)
        TODO("parse output")
    }

    protected val CHANNELS = 1;
    protected val neuralNetwork: MultiLayerNetwork = KerasModelImport.importKerasSequentialModelAndWeights(modelAndWeightsStream);
}
