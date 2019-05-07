package by.bsuir.luckymushroom.app.services.api.articles

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesService {
    @GET("/api/articles")
    fun getArticles(
        @Query("latitudeSeconds") latitudeSeconds: Int,
        @Query("longitudeSeconds") longitudeSeconds: Int
    )
}