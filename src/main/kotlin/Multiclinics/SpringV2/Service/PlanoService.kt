package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Plano
import Multiclinics.SpringV2.dto.PlanoRequest
import Multiclinics.SpringV2.dto.PlanoResponse
import Multiclinics.SpringV2.repository.ConvenioRepository
import Multiclinics.SpringV2.repository.PlanoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class PlanoService(
    val planoRepository: PlanoRepository,
    val convenioRepository: ConvenioRepository
) {

    fun validarLista(lista: List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }
    }

    private fun toResponse(plano: Plano): PlanoResponse {
        return PlanoResponse(
            id = plano.id,
            nome = plano.nome,
            descricao = plano.descricao,
            ativo = plano.ativo
        )
    }

    @Transactional
    fun criar(request: PlanoRequest, convenioId: Int): PlanoResponse {
        val convenio = convenioRepository.findById(convenioId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado") }
        
        val plano = Plano(
            nome = request.nome,
            descricao = request.descricao,
            ativo = request.ativo,
            convenio = convenio
        )
        
        val savedPlano = planoRepository.save(plano)
        return toResponse(savedPlano)
    }

    @Transactional
    fun atualizar(id: Int, request: PlanoRequest): ResponseEntity<PlanoResponse> {
        val planoExistente = planoRepository.findById(id)
        return if (planoExistente.isPresent) {
            val plano = planoExistente.get()
            plano.apply {
                nome = request.nome
                descricao = request.descricao
                ativo = request.ativo
            }
            val planoSalvo = planoRepository.save(plano)
            ResponseEntity.ok(toResponse(planoSalvo))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun deletar(id: Int) {
        if (!planoRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado")
        }
        planoRepository.deleteById(id)
    }

    fun listarTodos(): List<PlanoResponse> {
        val lista = planoRepository.findAll()
        validarLista(lista)
        return lista.map { toResponse(it) }
    }

    fun listarPorConvenio(convenioId: Int): List<PlanoResponse> {
        val lista = planoRepository.findByConvenioId(convenioId)
        validarLista(lista)
        return lista.map { toResponse(it) }
    }

    fun listarAtivosPorConvenio(convenioId: Int): List<PlanoResponse> {
        val lista = planoRepository.findPlanosAtivosByConvenioId(convenioId)
        validarLista(lista)
        return lista.map { toResponse(it) }
    }

    fun buscarPorId(id: Int): ResponseEntity<PlanoResponse> {
        val plano = planoRepository.findById(id)
        return if (plano.isPresent) {
            ResponseEntity.ok(toResponse(plano.get()))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun inativar(id: Int) {
        val plano = planoRepository.findById(id)
        if (plano.isPresent) {
            val planoInativado = plano.get()
            planoInativado.ativo = false
            planoRepository.save(planoInativado)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado")
        }
    }

    fun ativar(id: Int) {
        val plano = planoRepository.findById(id)
        if (plano.isPresent) {
            val planoAtivado = plano.get()
            planoAtivado.ativo = true
            planoRepository.save(planoAtivado)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Plano não encontrado")
        }
    }
}

