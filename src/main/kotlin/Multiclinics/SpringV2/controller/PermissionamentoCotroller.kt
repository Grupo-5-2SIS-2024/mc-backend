package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.PermissionamentoService
import Multiclinics.SpringV2.dominio.Paciente
import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.repository.PermissionamentoRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/permissionamentos")
class PermissionamentoCotroller(
    val permissionamentoRepository: PermissionamentoRepository,
    val permissaoService: PermissionamentoService
) {
    @CrossOrigin
    @PostMapping
    fun adicionarPermissao(@RequestBody novaPermissao: Permissionamento): ResponseEntity<Permissionamento> {
        permissaoService.salvar(novaPermissao)
        return ResponseEntity.status(201).body(novaPermissao)
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarPermissao(@PathVariable id: Int, @RequestBody @Valid novaPermissao: Permissionamento): ResponseEntity<*> {
        val permissaoAtualizado = permissaoService.atualizar(id, novaPermissao)
        return ResponseEntity.ok(permissaoAtualizado)
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarPermissao(@PathVariable id: Int): ResponseEntity<Permissionamento> {
            permissaoService.deletar(id)
            return ResponseEntity.status(200).build()

    }

    @CrossOrigin
    @GetMapping
    fun listarPermissao(): ResponseEntity<List<Permissionamento>> {
        val permissoes = permissaoService.getLista()
        return ResponseEntity.status(200).body(permissoes)
    }

}