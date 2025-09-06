package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.Paciente
import Multiclinics.SpringV2.repository.EnderecoRespository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enderecos")
class EnderecoController(
    val enderecoRespository: EnderecoRespository
) {
    @CrossOrigin
    @PostMapping
    fun adicionarEndereco(@RequestBody novoEndereco: Endereco): ResponseEntity<Endereco> {
        val EnderecoExistente = enderecoRespository.findByCep(novoEndereco.cep?:"")
        return if (EnderecoExistente != null) {
            ResponseEntity.status(401).build()
        } else {
            enderecoRespository.save(novoEndereco)
            ResponseEntity.status(201).body(novoEndereco)
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarEndereco(@PathVariable id: Int, @RequestBody @Valid novoEndereco: Endereco): ResponseEntity<Endereco> {
        val EnderecoExistente = enderecoRespository.findById(id)
        if (EnderecoExistente.isPresent) {
            val EnderecoEscolhido = EnderecoExistente.get()

            // Atualiza os dados do médico existente com os novos dados

            EnderecoEscolhido.cep = novoEndereco.cep



            val EnderecoAtualizado = enderecoRespository.save(EnderecoEscolhido)
            return ResponseEntity.status(200).body(EnderecoAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarEndereco(@PathVariable id: Int): ResponseEntity<Endereco> {
        if (enderecoRespository.existsById(id)) {
            enderecoRespository.deleteById(id)
            return ResponseEntity.status(200).build()
        }
        return ResponseEntity.status(404).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarEndereco(): ResponseEntity<List<Endereco>> {
        val enderecos = enderecoRespository.findAll()
        return ResponseEntity.status(200).body(enderecos)
    }
}