package by.bsuir.luckymushroom.app

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import by.bsuir.luckymushroom.R

class RecognitionResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognition_result)

        val imageBitmap = intent.getParcelableExtra<Bitmap>(MainActivity.EXTRA_IMAGE)

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(imageBitmap)

    }
}
