package by.bsuir.luckymushroom.app.services.api.recognitionRequests

import by.bsuir.luckymushroom.app.dto.recognitionRequests.RecognitionRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GetRecognitionRequestsService {
    @GET("/api/recognitionRequests")
    fun getRecognitionRequests(
        @Header("Cookie") cookie: String
    ): Deferred<Response<Array<RecognitionRequest>>>
}