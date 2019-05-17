package by.bsuir.luckymushroom.app.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.dto.articles.Article

class ArticlesRecyclerViewAdapter(private val dataset: Array<Article>) :
    RecyclerView.Adapter<ArticlesRecyclerViewAdapter.ArticlesViewHolder>() {
    class ArticlesViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticlesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_article, parent, false)

        return ArticlesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.articleTitle).apply {
            text = dataset[position].articleTitle
        }
        holder.view.findViewById<TextView>(R.id.articleText).apply {
            text = dataset[position].articleText
        }

    }

    override fun getItemCount(): Int = dataset.size
}