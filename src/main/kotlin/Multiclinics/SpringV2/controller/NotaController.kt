package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.NotasService
import Multiclinics.SpringV2.dominio.Notas

import Multiclinics.SpringV2.repository.NotasRepository

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notas")
class NotaController (
    val notasRepository: NotasRepository,
    val notasService: NotasService
){

    @PostMapping
    fun adicionarNota(@RequestBody novaNota: Notas): ResponseEntity<Notas> {

        notasService.salvar(novaNota)
        return ResponseEntity.status(201).body(novaNota)

    }

    @PutMapping("/{id}")
    fun atualizarNota(@PathVariable id: Int, @RequestBody @Valid novaNota: Notas): ResponseEntity<*> {
            val NotaAtualizado = notasService.atualizar(id, novaNota)
            return ResponseEntity.ok(NotaAtualizado)

    }


    @DeleteMapping("/{id}")
    fun deletarNota(@PathVariable id: Int): ResponseEntity<Notas> {
        notasService.deletar(id)
        return ResponseEntity.status(200).build()
    }

    @GetMapping
    fun listarNota(): ResponseEntity<List<Notas>> {
        val notas = notasService.getLista()
        return ResponseEntity.status(200).body(notas)
    }
}