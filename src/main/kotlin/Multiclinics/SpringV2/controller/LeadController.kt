package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.dominio.PossivelCliente
import Multiclinics.SpringV2.repository.PossivelClienteRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/leads")
class LeadController(
    val leadRepository: PossivelClienteRepository
) {
    @CrossOrigin
    @PostMapping
    fun adicionarLead(@RequestBody novoLead: PossivelCliente): ResponseEntity<PossivelCliente> {
        val LeadExistente = leadRepository.findByEmail(novoLead.email?:"")
        return if (LeadExistente != null) {
            ResponseEntity.status(401).build()
        } else {
            leadRepository.save(novoLead)
            ResponseEntity.status(201).body(novoLead)
        }
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarLead(@PathVariable id: Int, @RequestBody @Valid novoLead: PossivelCliente): ResponseEntity<PossivelCliente> {
        val LeadExistente = leadRepository.findById(id)
        if (LeadExistente.isPresent) {
            val LeadEscolhido = LeadExistente.get()

            // Atualiza os dados do médico existente com os novos dados
            LeadEscolhido.nome = novoLead.nome
            LeadEscolhido.sobrenome = novoLead.sobrenome
            LeadEscolhido.email = novoLead.email
            LeadEscolhido.cpf = novoLead.cpf
            LeadEscolhido.telefone = novoLead.telefone
            LeadEscolhido.dataNascimento = novoLead.dataNascimento



            val LeadAtualizado = leadRepository.save(LeadEscolhido)
            return ResponseEntity.status(200).body(LeadAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarLead(@PathVariable id: Int): ResponseEntity<PossivelCliente> {
        if (leadRepository.existsById(id)) {
            leadRepository.deleteById(id)
            return ResponseEntity.status(200).build()
        }
        return ResponseEntity.status(404).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarLead(): ResponseEntity<List<PossivelCliente>> {
        val leads = leadRepository.findAll()
        return ResponseEntity.status(200).body(leads)
    }

    @CrossOrigin
    @GetMapping("/percentual-convertidos")
    fun percentualLeadsConvertidos(): ResponseEntity<Double> {
        val percentual = leadRepository.percentualLeadsConvertidos()
        return ResponseEntity.ok(percentual)
    }
}