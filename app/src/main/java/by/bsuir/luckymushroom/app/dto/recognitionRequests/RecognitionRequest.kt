package by.bsuir.luckymushroom.app.dto.recognitionRequests

import java.sql.Timestamp
import java.util.*

data class RecognitionRequest(
    val requestId: Int?,
    val requestDatetime: Date,
    val edibleStatus: EdibleStatus?,
    val recognitionStatus: RecognitionStatus,
    val requestPhoto: RequestPhoto
)