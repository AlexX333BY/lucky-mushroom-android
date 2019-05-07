package by.bsuir.luckymushroom.app.services.api.recognitionRequests

import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.recognitionRequests.RecognitionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AddRecognitionRequestService {
    @POST("/api/recognitionRequests/add")
    fun addRecognitionRequest(
        @Body recognitionRequest: RecognitionRequest,
        @Header("Cookie") cookie: String = App.cookie
    ): Call<RecognitionRequest>
}