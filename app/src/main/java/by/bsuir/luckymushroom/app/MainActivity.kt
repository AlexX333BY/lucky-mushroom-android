package by.bsuir.luckymushroom.app

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import by.bsuir.luckymushroom.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val REQUEST_TAKE_PHOTO = 1
        val EXTRA_IMAGE = "Image"
    }


    lateinit var currentPhotoPath: String
    lateinit var photoURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        buttonPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, RecognitionResultActivity::class.java).also {
                it.putExtra(EXTRA_IMAGE, photoURI)
            }
            startActivity(intent)
        }

    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    val toast = Toast.makeText(
                        this,
                        "Уважаемый, ошибка ${ex.message}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI =
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) FileProvider.getUriForFile(
                            this,
                            "by.bsuir.luckymushroom.fileprovider",
                            it
                        ) else Uri.fromFile(it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)


                }


            }
        }
    }


}
