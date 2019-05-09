package by.bsuir.luckymushroom.app.ui

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.recognitionRequests.EdibleStatus
import by.bsuir.luckymushroom.app.dto.recognitionRequests.RecognitionRequest
import by.bsuir.luckymushroom.app.dto.recognitionRequests.RecognitionStatus
import by.bsuir.luckymushroom.app.dto.recognitionRequests.RequestPhoto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.sql.Timestamp

class RecognitionResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_recognition_result, container, false
        ).apply {
            val photoUri: Uri? =
                arguments?.getParcelable(MainActivity.EXTRA_IMAGE)
            val recognitionResultText: String =
                arguments!!.getString(MainActivity.EXTRA_TEXT)
            val imageView = findViewById<ImageView>(R.id.imageView).apply {
                setImageBitmap(null)
                setImageURI(
                    photoUri
                )
            }
            findViewById<TextView>(R.id.textView).text = recognitionResultText
            val edibleStatus: EdibleStatus =
                if (recognitionResultText == "not-recognized") EdibleStatus(
                    "non-edible"
                ) else EdibleStatus(
                    recognitionResultText
                )
            val recognitionStatus: RecognitionStatus =
                if (recognitionResultText != "not-recognized") RecognitionStatus(
                    "recognized"
                ) else RecognitionStatus(recognitionResultText)
            App.cookie?.let { cookie ->
                photoUri?.let { photoUri ->
                    File(photoUri.path).also {

                        val base64Image =
                            Base64.encodeToString(
                                it.readBytes(), Base64.DEFAULT
                            )

//                            .also {
                        App.addRecognitionRequestService.addRecognitionRequest(
                            RecognitionRequest(
                                null, Timestamp(
                                    System.currentTimeMillis()

                                ), edibleStatus, recognitionStatus,
                                arrayOf(
                                    RequestPhoto("jpg", base64Image)
                                )
                            ), cookie
                        )
                            .enqueue(object : Callback<RecognitionRequest> {
                                override fun onFailure(
                                    call: Call<RecognitionRequest>,
                                    t: Throwable
                                ) {

                                }

                                override fun onResponse(
                                    call: Call<RecognitionRequest>,
                                    response: Response<RecognitionRequest>
                                ) {
                                    response.body()
                                }


                            })

                    }
                }
            }

        }
    }


}


