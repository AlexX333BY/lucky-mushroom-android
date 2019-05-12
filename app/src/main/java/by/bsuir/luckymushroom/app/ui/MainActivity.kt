package by.bsuir.luckymushroom.app.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.nn.common.MushroomRecognizer
import by.bsuir.luckymushroom.nn.impl.NeuralMushroomRecognizer
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    RecognitionFragment.OnClickListener,
    LoginFragment.OnClickListener {

    companion object {
        val REQUEST_TAKE_PHOTO = 1
        val REQUEST_GET_IMAGE = 2
        val EXTRA_IMAGE = "Image"
        val EXTRA_TEXT = "Text"
    }

    lateinit var currentPhotoPath: String
    lateinit var photoURI: Uri
    lateinit var recognitionFragment: RecognitionFragment
    lateinit var infoFragment: InfoFragment
    lateinit var recognitionResultFragment: RecognitionResultFragment
    lateinit var loginFragment: LoginFragment
    var mReturningWithResult: Boolean = false
    var mFileFromGallery: Boolean = false
    var locationPermission: Boolean = false

    override fun runMain() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, recognitionFragment)
            .commit()

        initToggle()

        findViewById<NavigationView>(
            R.id.navigationView
        ).also { it.setNavigationItemSelectedListener(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1
        )


        recognitionFragment = RecognitionFragment()
        infoFragment = InfoFragment()
        recognitionResultFragment = RecognitionResultFragment()
        loginFragment = LoginFragment()

//        if (savedInstanceState == null) {

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_content, loginFragment).commit()

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_content, recognitionFragment)
//            .commit()
//        }

        initUser()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                locationPermission =
                    (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)


            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // TODO: debug
        Toast.makeText(this, item.title.toString(), Toast.LENGTH_LONG)
            .show()

        when (item.itemId) {
            R.id.nav_info -> {

                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_content, infoFragment
                ).addToBackStack(null).commit()
            }

            R.id.nav_recognition -> {

                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_content, recognitionFragment
                ).addToBackStack(null).commit()
            }
        }


        findViewById<DrawerLayout>(R.id.drawerLayoutMain).apply {
            closeDrawer(
                GravityCompat.START
            )
        }
        return true
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            mReturningWithResult = true
        } else if (requestCode == REQUEST_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.let {
                photoURI = getPath(it.data as Uri)!!
            }
            mReturningWithResult = true


        }

    }

    fun getPath(uri: Uri): Uri? {
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val fileId = DocumentsContract.getDocumentId(uri)
        val id = fileId.split(":")[1]
        val selector = MediaStore.Images.Media._ID + "=?"
        val cursor: Cursor =
            contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selector, arrayOf(id), null)
        var rez: Uri? = null
        if (cursor.moveToFirst()) {
            val columnIndex: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            val filePath: String = cursor.getString(columnIndex)
            rez = Uri.parse(filePath)
        }
        cursor.close()
        return rez

    }

    override fun onPostResume() {
        super.onPostResume()
        if (mReturningWithResult)
            recognize()

        mReturningWithResult = false
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }

    }

    private fun initToggle() {
        findViewById<DrawerLayout>(R.id.drawerLayoutMain).also {
            val toolbar = findViewById<Toolbar>(R.id.toolbar)

            val toggle = ActionBarDrawerToggle(
                this, it, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            it.addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun initUser() {
        if (App.user != null) {
            val toast = Toast.makeText(
                this, "hello ${App.user!!.userCredentials.userMail}",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    private fun recognize() {

        val modelFile = File.createTempFile("model_with_weights", ".tmp")
        modelFile.deleteOnExit()

        FileUtils.copyInputStreamToFile(
            assets.open("model_with_weights.pb"), modelFile
        )

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

                recognizer?.recognize(File(photoURI.path))
            } catch (ex: Error) {
                null
            } catch (ex: Exception) {
                null
            }

        val recognizeResultText =
            recognizeResult?.reduce { a, b -> if (a.probability > b.probability) a else b }
                ?.className ?: "not-recognized"

        Bundle().also {
            it.putParcelable(EXTRA_IMAGE, photoURI)
            it.putString(EXTRA_TEXT, recognizeResultText)

            recognitionResultFragment.arguments = it

        }


        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_content, recognitionResultFragment
            ).addToBackStack(null).commit()

    }

    override fun pickUpFromGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { getPictureIntent ->
            getPictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
            getPictureIntent.setType("image/*")
            startActivityForResult(
                Intent.createChooser(getPictureIntent, "Select Picture"),
                REQUEST_GET_IMAGE
            )
        }
    }

    override fun dispatchTakePictureIntent() {
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
                        )
                        else Uri.fromFile(it)
                    takePictureIntent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        photoURI
                    )
                    startActivityForResult(
                        takePictureIntent,
                        REQUEST_TAKE_PHOTO
                    )
                }

            }
        }
    }


}
