package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.dominio.SalaAtendimento
import Multiclinics.SpringV2.repository.SalaAtendimentoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/salas")
class SalaAtendimentoController(
    private val salaAtendimentoRepository: SalaAtendimentoRepository
) {

    @GetMapping
    fun listar(): ResponseEntity<List<SalaAtendimento>> {
        return ResponseEntity.ok(salaAtendimentoRepository.findAllByOrderByNomeAsc())
    }
}