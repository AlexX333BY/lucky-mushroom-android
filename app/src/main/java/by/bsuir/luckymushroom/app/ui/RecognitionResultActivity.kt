package by.bsuir.luckymushroom.app.ui

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.nn.common.*
import by.bsuir.luckymushroom.nn.impl.NeuralMushroomRecognizer
import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.Exception


class RecognitionResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognition_result)

        val imageFileUri = intent.getParcelableExtra<Uri>(MainActivity.EXTRA_IMAGE)

        val imageView = findViewById<ImageView>(by.bsuir.luckymushroom.R.id.imageView)
        imageView.setImageURI(imageFileUri)

        val modelFile = File.createTempFile("model_with_weights", ".tmp")
        modelFile.deleteOnExit()

        FileUtils.copyInputStreamToFile(assets.open("model_with_weights.pb"), modelFile)

        val labelsFile = File.createTempFile("labels", ".tmp")
        labelsFile.deleteOnExit()

        FileUtils.copyInputStreamToFile(assets.open("labels.txt"), labelsFile)
        val recognizeResult =

            try {
                var recognizer: MushroomRecognizer?
                recognizer = try {
                    NeuralMushroomRecognizer(modelFile, labelsFile)
                } catch (ex: Error) {
                    null
                }

                recognizer?.recognize(File(imageFileUri.path))
            } catch (ex: Error) {
                null
            } catch (ex: Exception) {
                null
            }
                val recognizeResultText =
            recognizeResult?.reduce { a, b -> if (a.probability > b.probability) a else b }?.className

        val toast = Toast.makeText(
            this,
            recognizeResultText,
            Toast.LENGTH_LONG
        )
        toast.show()

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = recognizeResultText

    }
}
