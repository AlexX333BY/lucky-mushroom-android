package by.bsuir.luckymushroom.nn.impl

import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.common.RecognitionResult
import java.io.File
import org.tensorflow.lite.Interpreter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class NeuralMushroomRecognizer(modelAndWeights: File) : MushroomRecognizer {
    override fun recognize(image: File): Array<RecognitionResult> {
        val labelProb = arrayOf(ByteArray(2), ByteArray(2))

        val bitmap = BitmapFactory.decodeStream(FileInputStream(image))
        neuralNetwork.run(convertBitmapToByteBuffer(bitmap), labelProb)
        TODO(
                "1. convert image to ResNet's 224 * 224" +
                "2. parse output" +
                "3. properly get labels"
        )
    }

    protected fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val imageBytes = ByteBuffer.allocateDirect(bitmap.byteCount)
        imageBytes.order(ByteOrder.nativeOrder())

        val pixels = IntArray(bitmap.height * bitmap.width)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        var value: Int
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                value = pixels[pixel++]
                imageBytes.put(((value shr 16) and 0xFF).toByte())
                imageBytes.put(((value shr 8) and 0xFF).toByte())
                imageBytes.put((value and 0xFF).toByte())
            }
        }

        return imageBytes
    }

    protected val neuralNetwork: Interpreter = Interpreter(modelAndWeights)
}
