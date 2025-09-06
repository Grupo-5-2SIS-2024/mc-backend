package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.ResponsavelService
import Multiclinics.SpringV2.dominio.Paciente
import Multiclinics.SpringV2.dominio.Responsavel
import Multiclinics.SpringV2.repository.ResponsavelRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/responsaveis")
class ResponsavelController(
    val responsavelRepository: ResponsavelRepository,
    val responsavelService: ResponsavelService
) {
    @CrossOrigin
    @PostMapping
    fun adicionarResponsavel(@RequestBody novoResponsavel: Responsavel): ResponseEntity<Responsavel> {
        responsavelService.salvar(novoResponsavel)
        return ResponseEntity.status(201).body(novoResponsavel)
    }
    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarResponsavel(@PathVariable id: Int, @RequestBody @Valid novoResponsavel: Responsavel): ResponseEntity<*> {

            val ResponsavelAtualizado = responsavelService.atualizar(id, novoResponsavel)
            return ResponseEntity.status(200).body(ResponsavelAtualizado)

    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarResponsavel(@PathVariable id: Int): ResponseEntity<Responsavel> {
        responsavelService.deletar(id)
        return ResponseEntity.status(200).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarResponsavel(): ResponseEntity<List<Responsavel>> {
        val responsaveis = responsavelService.getLista()
        return ResponseEntity.status(200).body(responsaveis)
    }

    @CrossOrigin
    @GetMapping("/cpf")
    fun buscarResponsavelPorCpf(@RequestParam cpf: String): ResponseEntity<Responsavel> {
        val responsavel = responsavelService.buscarPorCpf(cpf)
        return if (responsavel != null) {
            ResponseEntity.status(200).body(responsavel)
        } else {
            ResponseEntity.status(404).build()
        }
    }
}