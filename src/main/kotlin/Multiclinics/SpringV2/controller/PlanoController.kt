package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.PlanoService
import Multiclinics.SpringV2.dominio.Plano
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/planos")
class PlanoController(
    val planoService: PlanoService
) {

    // Função helper para verificar permissão
    private fun verificarPermissaoAdmin(nivelAcesso: String?) {
        if (nivelAcesso == null || !nivelAcesso.equals("Admin", ignoreCase = true)) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Acesso negado. Apenas administradores podem gerenciar planos."
            )
        }
    }

    @PostMapping("/convenio/{convenioId}")
    fun criar(
        @PathVariable convenioId: Int,
        @RequestBody @Valid novoPlano: Plano,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Plano> {
        verificarPermissaoAdmin(nivelAcesso)
        val planoCriado = planoService.criar(novoPlano, convenioId)
        return ResponseEntity.status(201).body(planoCriado)
    }

    @GetMapping
    fun listarTodos(): ResponseEntity<List<Plano>> {
        val planos = planoService.listarTodos()
        return ResponseEntity.ok(planos)
    }

    @GetMapping("/convenio/{convenioId}")
    fun listarPorConvenio(@PathVariable convenioId: Int): ResponseEntity<List<Plano>> {
        val planos = planoService.listarPorConvenio(convenioId)
        return ResponseEntity.ok(planos)
    }

    @GetMapping("/convenio/{convenioId}/ativos")
    fun listarAtivosPorConvenio(@PathVariable convenioId: Int): ResponseEntity<List<Plano>> {
        val planos = planoService.listarAtivosPorConvenio(convenioId)
        return ResponseEntity.ok(planos)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Plano> {
        return planoService.buscarPorId(id)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Int,
        @RequestBody @Valid planoAtualizado: Plano,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Plano> {
        verificarPermissaoAdmin(nivelAcesso)
        return planoService.atualizar(id, planoAtualizado)
    }

    @DeleteMapping("/{id}")
    fun deletar(
        @PathVariable id: Int,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Void> {
        verificarPermissaoAdmin(nivelAcesso)
        planoService.deletar(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/inativar")
    fun inativar(
        @PathVariable id: Int,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Void> {
        verificarPermissaoAdmin(nivelAcesso)
        planoService.inativar(id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/ativar")
    fun ativar(
        @PathVariable id: Int,
        @RequestHeader("Nivel-Acesso", required = false) nivelAcesso: String?
    ): ResponseEntity<Void> {
        verificarPermissaoAdmin(nivelAcesso)
        planoService.ativar(id)
        return ResponseEntity.ok().build()
    }
}

