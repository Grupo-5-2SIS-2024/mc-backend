package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.dominio.TipoDeContato
import Multiclinics.SpringV2.repository.TipoDeContatoRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tiposdecontatos")
class TipoDeContatoController(
    val tipoDeContatoRepository: TipoDeContatoRepository
) {
    @CrossOrigin
    @PostMapping
    fun adicionarTipo(@RequestBody novoTipo: TipoDeContato): ResponseEntity<TipoDeContato> {
        val TipoExistente = tipoDeContatoRepository.findByFaseContato(novoTipo.faseContato?:"")
        return if (TipoExistente != null) {
            ResponseEntity.status(401).build()
        } else {
            tipoDeContatoRepository.save(novoTipo)
            ResponseEntity.status(201).body(novoTipo)
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarTipo(@PathVariable id: Int, @RequestBody @Valid novoTipo: TipoDeContato): ResponseEntity<TipoDeContato> {
        val TipoExistente = tipoDeContatoRepository.findById(id)
        if (TipoExistente.isPresent) {
            val TipoEscolhido = TipoExistente.get()

            // Atualiza os dados do médico existente com os novos dados
            TipoEscolhido.faseContato = novoTipo.faseContato


            val TipoAtualizado = tipoDeContatoRepository.save(TipoEscolhido)
            return ResponseEntity.status(200).body(TipoAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarTipo(@PathVariable id: Int): ResponseEntity<TipoDeContato> {
        if (tipoDeContatoRepository.existsById(id)) {
            tipoDeContatoRepository.deleteById(id)
            return ResponseEntity.status(200).build()
        }
        return ResponseEntity.status(404).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarTipo(): ResponseEntity<List<TipoDeContato>> {
        val tipos = tipoDeContatoRepository.findAll()
        return ResponseEntity.status(200).body(tipos)
    }
}