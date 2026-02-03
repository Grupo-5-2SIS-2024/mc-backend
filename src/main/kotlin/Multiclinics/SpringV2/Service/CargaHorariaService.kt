package Multiclinics.SpringV2.Service
import Multiclinics.SpringV2.dominio.CargaHoraria
import Multiclinics.SpringV2.dto.CargaHorariaRequest
import Multiclinics.SpringV2.repository.CargaHorariaRepository
import Multiclinics.SpringV2.repository.MedicoRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
@Service
class CargaHorariaService(
    val cargaHorariaRepository: CargaHorariaRepository,
    val medicoRepository: MedicoRepository
) {
    @Transactional
    fun substituirEmLoteDto(medicoId: Int, lista: List<CargaHorariaRequest>) {
        val medico = medicoRepository.findById(medicoId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        lista.forEach {
            if (it.horaInicio >= it.horaFim) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        cargaHorariaRepository.deleteByMedicoId(medicoId)

        val entidades = lista.map {
            CargaHoraria(
                diaSemana = it.diaSemana,
                horaInicio = it.horaInicio,
                horaFim = it.horaFim,
                medico = medico
            )
        }

        cargaHorariaRepository.saveAll(entidades)
    }

    @Transactional
    fun deletarPorMedico(medicoId: Int) {
        // Se não existir nada, ok
        cargaHorariaRepository.deleteByMedicoId(medicoId)
    }



    fun listarPorMedico(medicoId: Int): List<CargaHoraria> {
        return cargaHorariaRepository.findByMedicoId(medicoId)
    }

    fun deletar(id: Int) {
        if (!cargaHorariaRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        cargaHorariaRepository.deleteById(id)
    }

}