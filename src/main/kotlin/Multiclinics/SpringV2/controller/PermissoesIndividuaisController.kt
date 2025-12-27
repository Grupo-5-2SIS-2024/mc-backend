package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.PermissoesIndividuaisService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/permissoes-individuais")
@CrossOrigin(origins = ["*"])
class PermissoesIndividuaisController(
    private val service: PermissoesIndividuaisService
) {

    @PostMapping("/salvar")
    fun salvarPermissoes(@RequestBody payload: PermissoesPayload): ResponseEntity<Any> {
        return try {
            val permissoes = service.salvarPermissoes(payload.medicoId, payload.permissoes)
            ResponseEntity.ok(mapOf("mensagem" to "Permissões salvas com sucesso", "data" to permissoes))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("erro" to e.message))
        }
    }

    @GetMapping("/buscar/{medicoId}")
    fun buscarPermissoes(@PathVariable medicoId: Int): ResponseEntity<Any> {
        return try {
            val permissoes = service.buscarPermissoes(medicoId)
            if (permissoes != null) {
                ResponseEntity.ok(mapOf("permissoes" to permissoes))
            } else {
                ResponseEntity.ok(mapOf("permissoes" to null))
            }
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("erro" to e.message))
        }
    }

    @DeleteMapping("/deletar/{medicoId}")
    fun deletarPermissoes(@PathVariable medicoId: Int): ResponseEntity<Any> {
        return try {
            service.deletarPermissoes(medicoId)
            ResponseEntity.ok(mapOf("mensagem" to "Permissões deletadas com sucesso"))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("erro" to e.message))
        }
    }
}

data class PermissoesPayload(
    val medicoId: Int,
    val permissoes: String
)