package by.bsuir.luckymushroom.app.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import by.bsuir.luckymushroom.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
                findViewById<Button>(R.id.buttonPhoto).apply {


                    setOnClickListener {
                        onClickListener.dispatchTakePictureIntent()

                        // TODO: debug
                        Toast.makeText(
                            activity, "Run", Toast.LENGTH_LONG
                        ).show()
                    }

                }

                findViewById<Button>(R.id.buttonGallery).apply {
                    setOnClickListener {
                        onClickListener.pickUpFromGallery()
                    }
                }
            }
    }


}
