package by.bsuir.luckymushroom.nn.impl

import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import java.io.File
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream
import java.io.InputStream

class NeuralMushroomRecognizer(modelAndWeights: InputStream, labelsList: InputStream) : MushroomRecognizer {
    protected val HEIGHT = 224
    protected val WIDTH = 224
    protected val PIXEL_SIZE = 3

    protected val INPUT_NODE_NAME = "input_1"
    protected val OUTPUT_NODE_NAME = "output"

    protected val classCount: Int
    protected val neuralNetwork = TensorFlowInferenceInterface(modelAndWeights)
    protected val labels = BufferedReader(labelsList).lines().collect(Collectors.toList())

    init {
        classCount = labels.size
    }

    override fun recognize(image: File): Array<RecognitionResult> {
        val probabilities = FloatArray(classCount)
        val bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(FileInputStream(image)), WIDTH, HEIGHT, false)

        neuralNetwork.feed(INPUT_NODE_NAME, convertBitmapToFloatArray(bitmap),
            1, WIDTH.toLong(), HEIGHT.toLong(), PIXEL_SIZE.toLong())
        neuralNetwork.run(arrayOf(OUTPUT_NODE_NAME))
        neuralNetwork.fetch(OUTPUT_NODE_NAME, probabilities)

        return Array(classCount) { classNo -> RecognitionResult(labels[classNo], probabilities[classNo]) }
    }

    protected fun convertBitmapToFloatArray(bitmap: Bitmap): FloatArray {
        if ((bitmap.width != WIDTH) || (bitmap.height != HEIGHT)) {
            throw IllegalArgumentException(String.format("Bitmap size should be of %dx%d size", WIDTH, HEIGHT))
        }

        val floatedImage = FloatArray(WIDTH * HEIGHT * PIXEL_SIZE)
        val pixels = IntArray(WIDTH * HEIGHT)
        bitmap.getPixels(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT)

        for ((index, pixel) in pixels.withIndex()) {
            floatedImage[PIXEL_SIZE * index] = ((pixel shr 16) and 0xFF).toFloat()
            floatedImage[PIXEL_SIZE * index + 1] = ((pixel shr 8) and 0xFF).toFloat()
            floatedImage[PIXEL_SIZE * index + 2] = (pixel and 0xFF).toFloat()
        }

        return floatedImage
    }
}
