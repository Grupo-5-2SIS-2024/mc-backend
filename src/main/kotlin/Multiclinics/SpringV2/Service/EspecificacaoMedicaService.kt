package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.EspecificacaoMedica
import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.repository.EspecificacaoMedicaRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class EspecificacaoMedicaService(
    val especificaoRepository: EspecificacaoMedicaRepository
) {
    fun validarLista(lista:List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun salvar(novaEspecificacao: EspecificacaoMedica):ResponseEntity<EspecificacaoMedica> {

        val especificacaoExistente = especificaoRepository.findByArea(novaEspecificacao.area?:"")
        return if (especificacaoExistente != null) {
            ResponseEntity.status(401).build()
        } else {
            especificaoRepository.save(novaEspecificacao)
            ResponseEntity.status(201).body(novaEspecificacao)
        }
    }
    fun atualizar(id: Int ,novaEspecificacao: EspecificacaoMedica): ResponseEntity<EspecificacaoMedica> {
        val EspecificacaoExistente = especificaoRepository.findById(id)
        if (EspecificacaoExistente.isPresent) {
            val EspecificacaoEscolhida = EspecificacaoExistente.get()
            EspecificacaoEscolhida.area = novaEspecificacao.area
            val especificacaoAtualizada = especificaoRepository.save(EspecificacaoEscolhida)
            return ResponseEntity.status(200).body(especificacaoAtualizada)

        }else {
            return ResponseEntity.status(404).build()
        }
    }
    fun deletar(id: Int) {
        if (!especificaoRepository.existsById(id)) {
            throw IllegalArgumentException("Especificação não encontrado")
        }

        especificaoRepository.deleteById(id)

    }

    fun getLista():List<EspecificacaoMedica> {
        val lista = especificaoRepository.findAll()
        validarLista(lista)

        return lista
    }
}