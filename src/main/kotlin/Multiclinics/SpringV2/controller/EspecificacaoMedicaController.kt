package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.EspecificacaoMedicaService
import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.EspecificacaoMedica
import Multiclinics.SpringV2.repository.EspecificacaoMedicaRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/especificacoes")
class EspecificacaoMedicaController(
    val especificaoRepository: EspecificacaoMedicaRepository,
    val especificacaoMedicaService: EspecificacaoMedicaService
) {
    @CrossOrigin
    @PostMapping
    fun adicionarEspecificacao(@RequestBody novaEspecificacao: EspecificacaoMedica): ResponseEntity<String> {
        especificacaoMedicaService.salvar(novaEspecificacao)
        return ResponseEntity.status(201).body("ok")
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarEspecificaca(@PathVariable id: Int, @RequestBody @Valid novaEspecificacao: EspecificacaoMedica): ResponseEntity<*> {

            // Atualiza os dados do médico existente com os novos dados
            val EspecificacaoAtualizado = especificacaoMedicaService.atualizar(id, novaEspecificacao)
            return ResponseEntity.ok(EspecificacaoAtualizado)

    }
    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarEspecificacao(@PathVariable id: Int): ResponseEntity<EspecificacaoMedica> {
        especificacaoMedicaService.deletar(id)
        return ResponseEntity.status(200).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarEspecificacao(): ResponseEntity<List<EspecificacaoMedica>> {
        val especificacoes = especificacaoMedicaService.getLista()
        return ResponseEntity.status(200).body(especificacoes)
    }
}