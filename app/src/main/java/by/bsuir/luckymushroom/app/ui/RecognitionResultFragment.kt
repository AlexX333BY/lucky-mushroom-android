package by.bsuir.luckymushroom.app.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import by.bsuir.luckymushroom.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RecognitionResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_recognition_result, container, false
        ).apply {
            val imageView = findViewById<ImageView>(R.id.imageView).apply {
                setImageBitmap(null)
                setImageURI(
                    arguments?.getParcelable(MainActivity.EXTRA_IMAGE)
                )
            }
//            imageView.
            findViewById<TextView>(R.id.textView).text =
                arguments?.getString(MainActivity.EXTRA_TEXT)
        }
    }
}



