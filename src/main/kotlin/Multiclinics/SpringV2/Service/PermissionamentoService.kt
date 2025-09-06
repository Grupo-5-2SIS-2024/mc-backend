package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.EspecificacaoMedica
import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.repository.PermissionamentoRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PermissionamentoService(
    val permissionamentoRepository: PermissionamentoRepository
) {
    fun validarLista(lista:List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun salvar(novaPermissao: Permissionamento): ResponseEntity<Permissionamento> {

        val permissaoExistente = permissionamentoRepository.findByNome(novaPermissao.nome?:"")
        return if (permissaoExistente != null) {
            ResponseEntity.status(401).build()
        } else {
            permissionamentoRepository.save(novaPermissao)
            ResponseEntity.status(201).body(novaPermissao)
        }
    }
    fun atualizar(id: Int ,novaPermissao: Permissionamento): ResponseEntity<Permissionamento> {

        val PermissaoExistente = permissionamentoRepository.findById(id)
        if (PermissaoExistente.isPresent) {
            val PermissaoEscolhido = PermissaoExistente.get()

            // Atualiza os dados do médico existente com os novos dados
            PermissaoEscolhido.nome = novaPermissao.nome


            val PermissaoAtualizado = permissionamentoRepository.save(PermissaoEscolhido)
            return ResponseEntity.status(200).body(PermissaoAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }
    }
    fun deletar(id: Int) {
        if (!permissionamentoRepository.existsById(id)) {
            throw IllegalArgumentException("Pemissao não encontrada")
        }

        permissionamentoRepository.deleteById(id)

    }

    fun getLista():List<Permissionamento> {
        val lista = permissionamentoRepository.findAll()
        validarLista(lista)

        return lista
    }
}