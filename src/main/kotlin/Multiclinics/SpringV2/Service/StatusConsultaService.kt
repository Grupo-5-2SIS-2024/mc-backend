package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.dominio.StatusConsulta
import Multiclinics.SpringV2.repository.StatusRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class StatusConsultaService (
    val statusRepository: StatusRepository
){
    fun validarLista(lista:List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun salvar(novaStatus: StatusConsulta): ResponseEntity<StatusConsulta> {

        val statusExistente = statusRepository.findByNomeStatus(novaStatus.nomeStatus?:"")
        return if (statusExistente != null) {
            ResponseEntity.status(401).build()
        } else {
            statusRepository.save(novaStatus)
            ResponseEntity.status(201).body(novaStatus)
        }
    }
    fun atualizar(id: Int ,novaStatus: StatusConsulta): ResponseEntity<StatusConsulta> {

        val statusExistente = statusRepository.findById(id)
        if (statusExistente.isPresent) {
            val statusEscolhido = statusExistente.get()

            // Atualiza os dados do médico existente com os novos dados
            statusEscolhido.nomeStatus = novaStatus.nomeStatus


            val statusAtualizado = statusRepository.save(statusEscolhido)
            return ResponseEntity.status(200).body(statusAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }
    }
    fun deletar(id: Int) {
        if (!statusRepository.existsById(id)) {
            throw IllegalArgumentException("status não encontrado")
        }

        statusRepository.deleteById(id)

    }

    fun getLista():List<StatusConsulta> {
        val lista = statusRepository.findAll()
        validarLista(lista)

        return lista
    }

}