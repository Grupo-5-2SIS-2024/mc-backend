package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Convenio
import Multiclinics.SpringV2.dominio.Plano
import Multiclinics.SpringV2.dto.ConvenioRequest
import Multiclinics.SpringV2.dto.ConvenioResponse
import Multiclinics.SpringV2.dto.PlanoResponse
import Multiclinics.SpringV2.repository.ConvenioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class ConvenioService(
    val convenioRepository: ConvenioRepository
) {

    fun validarLista(lista: List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT)
        }
    }

    private fun toResponse(convenio: Convenio): ConvenioResponse {
        return ConvenioResponse(
            id = convenio.id,
            nome = convenio.nome,
            descricao = convenio.descricao,
            ativo = convenio.ativo,
            planos = convenio.planos.map { plano ->
                PlanoResponse(
                    id = plano.id,
                    nome = plano.nome,
                    descricao = plano.descricao,
                    ativo = plano.ativo
                )
            }
        )
    }

    @Transactional
    fun criar(request: ConvenioRequest): ConvenioResponse {
        // Verifica se já existe um convênio com o mesmo nome
        if (convenioRepository.existsByNome(request.nome)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Convênio com este nome já existe")
        }
        
        val convenio = Convenio(
            nome = request.nome,
            descricao = request.descricao,
            ativo = request.ativo
        )
        
        // Adiciona os planos se fornecidos
        request.planos?.forEach { planoReq ->
            val plano = Plano(
                nome = planoReq.nome,
                descricao = planoReq.descricao,
                ativo = planoReq.ativo,
                convenio = convenio
            )
            convenio.planos.add(plano)
        }
        
        val savedConvenio = convenioRepository.save(convenio)
        return toResponse(savedConvenio)
    }

    @Transactional
    fun atualizar(id: Int, request: ConvenioRequest): ResponseEntity<ConvenioResponse> {
        val convenioExistente = convenioRepository.findById(id)
        return if (convenioExistente.isPresent) {
            val convenio = convenioExistente.get()
            convenio.apply {
                nome = request.nome
                descricao = request.descricao
                ativo = request.ativo
            }
            val convenioSalvo = convenioRepository.save(convenio)
            ResponseEntity.ok(toResponse(convenioSalvo))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun deletar(id: Int) {
        if (!convenioRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado")
        }
        convenioRepository.deleteById(id)
    }

    fun listarTodos(): List<ConvenioResponse> {
        val lista = convenioRepository.findAllByOrderByNomeAsc()
        validarLista(lista)
        return lista.map { toResponse(it) }
    }

    fun listarAtivos(): List<ConvenioResponse> {
        val lista = convenioRepository.findAllAtivosOrdenados()
        validarLista(lista)
        return lista.map { toResponse(it) }
    }

    fun buscarPorId(id: Int): ResponseEntity<ConvenioResponse> {
        val convenio = convenioRepository.findById(id)
        return if (convenio.isPresent) {
            ResponseEntity.ok(toResponse(convenio.get()))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun inativar(id: Int) {
        val convenio = convenioRepository.findById(id)
        if (convenio.isPresent) {
            val convenioInativado = convenio.get()
            convenioInativado.ativo = false
            convenioRepository.save(convenioInativado)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado")
        }
    }

    fun ativar(id: Int) {
        val convenio = convenioRepository.findById(id)
        if (convenio.isPresent) {
            val convenioAtivado = convenio.get()
            convenioAtivado.ativo = true
            convenioRepository.save(convenioAtivado)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado")
        }
    }
}

