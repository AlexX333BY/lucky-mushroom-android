package by.bsuir.luckymushroom.app.ui.activities

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.ui.fragments.*
import by.bsuir.luckymushroom.app.viewmodels.AppViewModel
import by.bsuir.luckymushroom.app.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.*
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

    private lateinit var currentPhotoPath: String
    private lateinit var recognitionFragment: RecognitionFragment
    private lateinit var infoFragment: InfoFragment
    private lateinit var model: AppViewModel
    private lateinit var userModel: UserViewModel
    var locationPermission: Boolean = false
    private val isOldAndroidVersion: Boolean =
        Build.VERSION.SDK_INT <= Build.VERSION_CODES.M

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
        val logoutItem = navigationView.menu.findItem(R.id.nav_logout)
        val historyItem = navigationView.menu.findItem(R.id.nav_history)
        val loginItem = navigationView.menu.findItem(R.id.nav_login)

        model = ViewModelProviders.of(this).get(AppViewModel::class.java)
        userModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        model.getIsLoading().observe(this, Observer {
            it?.let {
                if (it) {
                    fragment_content.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = ProgressBar.INVISIBLE
                    fragment_content.visibility = View.VISIBLE
                }
            }
        })
        model.getIsRecognition().observe(this, Observer {
            it?.let {
                if (it)
                    model.launchRecognize()
            }
        })
        model.getRecognitionResult().observe(this, Observer {
            val recognizeResultText = it?.className ?: "not-recognized"

            openRecognitionResultFragment(recognizeResultText)
        })

        userModel.getUser().observe(this, Observer {
            if (it == null) {
                logoutItem.isVisible = false
                historyItem.isVisible = false
                loginItem.isVisible = true
                openLoginFragment()

            } else {
                logoutItem.isVisible = true
                historyItem.isVisible = true
                loginItem.isVisible = false
                runMain()
            }

            textViewUserName.text =
                it?.userCredentials?.userMail ?: "Mushroomer"
        })

        recognitionFragment = RecognitionFragment()
        infoFragment = InfoFragment()

        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_content,
                LoginFragment()
            ).commit()



        initToggle()

        findViewById<NavigationView>(
            R.id.navigationView
        ).also { it.setNavigationItemSelectedListener(this) }

        openLoginFragment()
    }

    override fun runMain() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content, recognitionFragment)
            .commit()

        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        model.launchRecognizerInit(assets)
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
                    (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_info -> {
                val frag = supportFragmentManager.findFragmentById(
                    R.id.fragment_content
                )
                if (!(frag != null && frag is InfoFragment && frag.isVisible)) {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_content, infoFragment
                    ).addToBackStack(null).commit()
                }
            }
            R.id.nav_recognition -> {
                val frag = supportFragmentManager.findFragmentById(
                    R.id.fragment_content
                )
                if (!(frag != null && frag is RecognitionFragment && frag.isVisible)) {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_content, recognitionFragment
                    ).addToBackStack(null).commit()
                }
            }
            R.id.nav_history -> {
                val frag = supportFragmentManager.findFragmentById(
                    R.id.fragment_content
                )
                if (!(frag != null && frag is HistoryFragment && frag.isVisible)) {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_content, HistoryFragment()
                    ).addToBackStack(null).commit()
                }
            }
            R.id.nav_logout -> {
                userModel.launchLogOut()
            }
            R.id.nav_login -> {
                openLoginFragment()
            }
        }

        findViewById<DrawerLayout>(R.id.drawerLayoutMain).apply {
            closeDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            model.setIsRecognition(true)
        } else if (requestCode == REQUEST_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.let {
                model.photoURI = getPath(it.data as Uri)!!
            }
            model.setIsRecognition(true)

        }
    }

    private fun openLoginFragment() {
        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        userModel.clearAuthError()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_content,
                LoginFragment()
            ).commit()
    }

    private fun getPath(uri: Uri): Uri? {
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = if (!isOldAndroidVersion) {
            val fileId = DocumentsContract.getDocumentId(uri)
            val id = fileId.split(":")[1]
            val selector = MediaStore.Images.Media._ID + "=?"

            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selector, arrayOf(id), null
            )
        } else {
            contentResolver.query(uri, projection, null, null, null)
        }
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }

    }

    private fun initToggle() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayoutMain, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayoutMain.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun openRecognitionResultFragment(recognizeResultText: String) {
        val recognitionResultFragment =
            RecognitionResultFragment()
        Bundle().also {
            it.putParcelable(
                EXTRA_IMAGE, model.photoURI
            )
            it.putString(
                EXTRA_TEXT, recognizeResultText
            )

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
            getPictureIntent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(getPictureIntent, "Select Picture"),
                REQUEST_GET_IMAGE
            )
        }
    }

    override fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION)
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    val toast = Toast.makeText(
                        this,
                        "Уважаемый, ошибка ${ex.message}",
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    null
                }
                photoFile?.also {
                    model.photoURI =
                        if (!isOldAndroidVersion) FileProvider.getUriForFile(
                            this,
                            "by.bsuir.luckymushroom.fileprovider",
                            it
                        )
                        else Uri.fromFile(it)
                    takePictureIntent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        model.photoURI
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
