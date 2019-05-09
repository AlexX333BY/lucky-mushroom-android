package by.bsuir.luckymushroom.app.dto.recognitionRequests

import java.sql.Timestamp

data class RecognitionRequest(
    val requestId: Int?,
    val requestDatetime: Timestamp,
    val edibleStatus: EdibleStatus?,
    val recognitionStatus: RecognitionStatus,
    val requestPhotos: Array<RequestPhoto>
)