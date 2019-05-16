package by.bsuir.luckymushroom.app.dto.articles

data class Article(
    val articleTitle: String,
    val articleText: String,
    val gpsTags: Array<GpsTag>? = null,
    val articleId: Int? = null

    )