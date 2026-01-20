package Multiclinics.SpringV2.dto
import Multiclinics.SpringV2.dominio.DiaSemana
import java.time.LocalTime

data class CargaHorariaRequest(
    val diaSemana: DiaSemana,
    val horaInicio: LocalTime,
    val horaFim: LocalTime

)