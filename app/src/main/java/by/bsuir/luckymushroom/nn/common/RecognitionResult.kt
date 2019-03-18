package by.bsuir.luckymushroom.nn.common

data class RecognitionResult(val isMushroom: Boolean, val edibility: Edibility? = null, val probability: Float? = null,
                             val mushroomId: Long? = null)
