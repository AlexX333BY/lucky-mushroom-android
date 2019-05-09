package by.bsuir.luckymushroom.app.services.api.articles

import by.bsuir.luckymushroom.app.dto.articles.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesService {
    @GET("/api/articles")
    fun getArticles(
        @Query("latitudeSeconds") latitudeSeconds: Int,
        @Query("longitudeSeconds") longitudeSeconds: Int
    ): Call<Array<Article>>
}