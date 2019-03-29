package by.bsuir.luckymushroom.nn.impl

import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import java.io.File
import org.tensorflow.lite.Interpreter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream

class NeuralMushroomRecognizer(modelAndWeights: File) : MushroomRecognizer {
    protected val HEIGHT = 224 // better to parse from some JSON?
    protected val WIDTH = 224  // better to parse from some JSON?
    protected val PIXEL_SIZE = 3

    override fun recognize(image: File): Array<RecognitionResult> {
        val labelProb = arrayOf(ByteArray(1), ByteArray(1))

        val bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(FileInputStream(image)), WIDTH, HEIGHT, false)

        neuralNetwork.run(convertBitmapToByteBuffer(bitmap), labelProb)
        TODO(
                "+1. convert image to ResNet's 224 * 224" +
                "2. parse output" +
                "3. properly get labels"
        )
    }

    protected fun convertBitmapToByteBuffer(bitmap: Bitmap): Array<Array<FloatArray>> {
        if ((bitmap.width != WIDTH) || (bitmap.height != HEIGHT)) {
            throw IllegalArgumentException(String.format("Bitmap size should be of %dx%d size", WIDTH, HEIGHT))
        }

        val floatedImage = Array(WIDTH) { Array(HEIGHT) { FloatArray(PIXEL_SIZE) } }
        val pixels = IntArray(WIDTH * HEIGHT)
        bitmap.getPixels(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT)

        var currentPixel = 0
        var currentPixelValue: Int
        for (i in 0 until WIDTH) {
            for (j in 0 until HEIGHT) {
                currentPixelValue = pixels[currentPixel++]
                floatedImage[i][j][0] = ((currentPixelValue shr 16) and 0xFF).toFloat()
                floatedImage[i][j][1] = ((currentPixelValue shr 8) and 0xFF).toFloat()
                floatedImage[i][j][2] = (currentPixelValue and 0xFF).toFloat()
            }
        }

        return floatedImage
    }

    protected val neuralNetwork: Interpreter = Interpreter(modelAndWeights)
}
