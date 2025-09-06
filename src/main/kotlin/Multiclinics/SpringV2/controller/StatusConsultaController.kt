package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.StatusConsultaService
import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.StatusConsulta
import Multiclinics.SpringV2.repository.StatusRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/statusConsultas")
class StatusConsultaController(
    var StatusRepository : StatusRepository,
    val statusConsultaService: StatusConsultaService
) {
    @CrossOrigin
    @PostMapping
    fun adicionarStatus(@RequestBody novoStatus: StatusConsulta): ResponseEntity<StatusConsulta> {

        statusConsultaService.salvar(novoStatus)
        return ResponseEntity.status(201).body(novoStatus)


    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarStatus(@PathVariable id: Int, @RequestBody @Valid novoStatus: StatusConsulta): ResponseEntity<*> {
        val statusAtualizado = statusConsultaService.atualizar(id, novoStatus)
        return ResponseEntity.ok(statusAtualizado)
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarStatus(@PathVariable id: Int): ResponseEntity<StatusConsulta> {
        statusConsultaService.deletar(id)
        return ResponseEntity.status(200).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarStatus(): ResponseEntity<List<StatusConsulta>> {
        val status = statusConsultaService.getLista()
        return ResponseEntity.status(200).body(status)
    }
}