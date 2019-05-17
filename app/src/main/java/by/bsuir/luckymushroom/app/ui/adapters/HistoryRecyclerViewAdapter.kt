package by.bsuir.luckymushroom.app.ui.adapters

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.dto.recognitionRequests.RecognitionRequest
import java.text.SimpleDateFormat

class HistoryRecyclerViewAdapter(private val dataset: Array<RecognitionRequest>) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_history, parent, false)

        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.view.findViewById<ImageView>(R.id.imageViewRecognizedPhoto)
            .apply {
                try {
                    val decodedByteArray = Base64.decode(
                        dataset[position].requestPhoto.photoData, Base64.DEFAULT
                    )
                    val bitmap = BitmapFactory.decodeByteArray(
                        decodedByteArray, 0, decodedByteArray.size
                    )
                    setImageBitmap(bitmap)
                } catch (ex: Exception) {
                }
            }
        holder.view.findViewById<TextView>(R.id.textViewTime).apply {
            text = SimpleDateFormat("d-MMM-yyyy H:mm:ss").format(
                dataset[position].requestDatetime
            )
        }
        holder.view.findViewById<TextView>(R.id.textViewRecognitionStatus)
            .apply {
                val recognitionRequest = dataset[position]
                text = recognitionRequest.edibleStatus?.edibleStatusAlias
                    ?: recognitionRequest.recognitionStatus.recognitionStatusAlias
            }
    }

    override fun getItemCount(): Int = dataset.size

}