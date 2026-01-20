package Multiclinics.SpringV2.repository
import Multiclinics.SpringV2.dominio.CargaHoraria
import Multiclinics.SpringV2.dominio.DiaSemana
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalTime

interface CargaHorariaRepository: JpaRepository<CargaHoraria, Int> {
    fun findByMedicoId(medicoId: Int): List<CargaHoraria>

    fun existsByMedicoIdAndDiaSemanaAndHoraInicioAndHoraFim(
        medicoId: Int,
        diaSemana: DiaSemana,
        horaInicio: LocalTime,
        horaFim: LocalTime
    ): Boolean
}