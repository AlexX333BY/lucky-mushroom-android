package by.bsuir.luckymushroom.app.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import by.bsuir.luckymushroom.R

class RecognitionFragment : Fragment() {
    interface OnClickListener {
        fun dispatchTakePictureIntent()
        fun pickUpFromGallery()
    }

    lateinit var onClickListener: OnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onClickListener = activity as OnClickListener
        return inflater.inflate(
            R.layout.fragment_recognition, container, false
        )
            .apply {
                findViewById<ImageButton>(R.id.buttonPhoto).apply {
                    setOnClickListener {
                        onClickListener.dispatchTakePictureIntent()
                        // TODO: debug
                        Toast.makeText(
                            activity, "Run", Toast.LENGTH_LONG
                        ).show()
                    }
                }

                findViewById<ImageButton>(R.id.buttonGallery).apply {
                    setOnClickListener {
                        onClickListener.pickUpFromGallery()
                    }
                }
            }
    }

}
