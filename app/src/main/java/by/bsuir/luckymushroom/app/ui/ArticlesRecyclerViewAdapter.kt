package by.bsuir.luckymushroom.app.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.dto.articles.Article

class ArticlesRecyclerViewAdapter(private val dataset: Array<Article>) :
    RecyclerView.Adapter<ArticlesRecyclerViewAdapter.ArticlesViewHolder>() {
    class ArticlesViewHolder(val textView: TextView) : RecyclerView.ViewHolder(
        textView
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticlesViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_view_article, parent, false) as TextView

        return ArticlesViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {

        holder.textView.text = dataset[position].articleText
    }

    override fun getItemCount(): Int = dataset.size
}