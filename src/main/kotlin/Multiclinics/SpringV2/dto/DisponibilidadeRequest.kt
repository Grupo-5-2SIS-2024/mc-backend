package Multiclinics.SpringV2.dto

import java.time.LocalTime
data class DisponibilidadeRequest (
    val medicoId: Int,
    val data: String,
    val duracaoMin: Int,
    val pacienteId: Int? = null
)

data class DisponibilidadeResponse(
    val horarios: List<String>
)