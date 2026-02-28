package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Plano
import Multiclinics.SpringV2.repository.ConvenioRepository
import Multiclinics.SpringV2.repository.PlanoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
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

    fun criar(novoPlano: Plano, convenioId: Int): Plano {
        val convenio = convenioRepository.findById(convenioId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado") }
        
        novoPlano.convenio = convenio
        return planoRepository.save(novoPlano)
    }

    fun atualizar(id: Int, planoAtualizado: Plano): ResponseEntity<Plano> {
        val planoExistente = planoRepository.findById(id)
        return if (planoExistente.isPresent) {
            val plano = planoExistente.get()
            plano.apply {
                nome = planoAtualizado.nome
                descricao = planoAtualizado.descricao
                ativo = planoAtualizado.ativo
            }
            val planoSalvo = planoRepository.save(plano)
            ResponseEntity.ok(planoSalvo)
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

    fun listarTodos(): List<Plano> {
        val lista = planoRepository.findAll()
        validarLista(lista)
        return lista
    }

    fun listarPorConvenio(convenioId: Int): List<Plano> {
        val lista = planoRepository.findByConvenioId(convenioId)
        validarLista(lista)
        return lista
    }

    fun listarAtivosPorConvenio(convenioId: Int): List<Plano> {
        val lista = planoRepository.findPlanosAtivosByConvenioId(convenioId)
        validarLista(lista)
        return lista
    }

    fun buscarPorId(id: Int): ResponseEntity<Plano> {
        val plano = planoRepository.findById(id)
        return if (plano.isPresent) {
            ResponseEntity.ok(plano.get())
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

