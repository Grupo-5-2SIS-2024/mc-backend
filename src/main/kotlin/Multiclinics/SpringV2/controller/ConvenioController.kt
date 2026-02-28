package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.ConvenioService
import Multiclinics.SpringV2.dominio.Convenio
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/convenios")
class ConvenioController(
    val convenioService: ConvenioService
) {

    // Função helper para verificar permissão
    private fun verificarPermissaoAdmin(nivelAcesso: String?) {
        if (nivelAcesso == null || !nivelAcesso.equals("Admin", ignoreCase = true)) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Acesso negado. Apenas administradores podem gerenciar convênios."
            )
        }
    }

    @GetMapping
    fun listarTodos(): ResponseEntity<List<Convenio>> {
        val convenios = convenioService.listarTodos()
        return ResponseEntity.ok(convenios)
    }

    @GetMapping("/ativos")
    fun listarAtivos(): ResponseEntity<List<Convenio>> {
        val convenios = convenioService.listarAtivos()
        return ResponseEntity.ok(convenios)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Convenio> {
        return convenioService.buscarPorId(id)
    }

    @PostMapping
    fun criar(
        @RequestBody @Valid novoConvenio: Convenio,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Convenio> {
        verificarPermissaoAdmin(nivelAcesso)
        val convenioCriado = convenioService.criar(novoConvenio)
        return ResponseEntity.status(201).body(convenioCriado)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @RequestBody @Valid convenioAtualizado: Convenio,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Convenio> {
        verificarPermissaoAdmin(nivelAcesso)
        return convenioService.atualizar(id, convenioAtualizado)
    }

    @DeleteMapping("/{id}")
    fun deletar(
        @PathVariable id: Int,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Void> {
        verificarPermissaoAdmin(nivelAcesso)
        convenioService.deletar(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/inativar")
    fun inativar(
        @PathVariable id: Int,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Void> {
        verificarPermissaoAdmin(nivelAcesso)
        convenioService.inativar(id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/ativar")
    fun ativar(
        @PathVariable id: Int,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Void> {
        verificarPermissaoAdmin(nivelAcesso)
        convenioService.ativar(id)
        return ResponseEntity.ok().build()
    }
}

