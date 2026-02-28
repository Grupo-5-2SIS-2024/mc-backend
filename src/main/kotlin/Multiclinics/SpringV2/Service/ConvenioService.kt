package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Convenio
import Multiclinics.SpringV2.repository.ConvenioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
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

    fun criar(novoConvenio: Convenio): Convenio {
        // Verifica se já existe um convênio com o mesmo nome
        if (novoConvenio.nome?.let { convenioRepository.existsByNome(it) } == true) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Convênio com este nome já existe")
        }
        return convenioRepository.save(novoConvenio)
    }

    fun atualizar(id: Int, convenioAtualizado: Convenio): ResponseEntity<Convenio> {
        val convenioExistente = convenioRepository.findById(id)
        return if (convenioExistente.isPresent) {
            val convenio = convenioExistente.get()
            convenio.apply {
                nome = convenioAtualizado.nome
                descricao = convenioAtualizado.descricao
                ativo = convenioAtualizado.ativo
            }
            val convenioSalvo = convenioRepository.save(convenio)
            ResponseEntity.ok(convenioSalvo)
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

    fun listarTodos(): List<Convenio> {
        val lista = convenioRepository.findAllByOrderByNomeAsc()
        validarLista(lista)
        return lista
    }

    fun listarAtivos(): List<Convenio> {
        val lista = convenioRepository.findAllAtivosOrdenados()
        validarLista(lista)
        return lista
    }

    fun buscarPorId(id: Int): ResponseEntity<Convenio> {
        val convenio = convenioRepository.findById(id)
        return if (convenio.isPresent) {
            ResponseEntity.ok(convenio.get())
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

