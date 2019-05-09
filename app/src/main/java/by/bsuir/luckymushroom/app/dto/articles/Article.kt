package by.bsuir.luckymushroom.app.dto.articles

data class Article(
    val articleText: String,
    val articleId: Int?,
    val gpsTags: Array<GpsTag>
)