package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.MedicoService
import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dto.GraficoGeralAdm
import Multiclinics.SpringV2.repository.ConsultaRepository
import Multiclinics.SpringV2.repository.MedicoRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/medicos")
class MedicoController(
    val medicoRepository: MedicoRepository,
    val medicoService: MedicoService
) {

    @CrossOrigin
    @PutMapping("/login")
    fun logarMedico(@RequestBody medico: Medico): ResponseEntity<Medico> {
        val medicoLogado = medicoRepository.findByEmailAndSenha(medico.email ?: "", medico.senha ?: "")

        return if (medicoLogado != null) {
            medicoLogado.ativo = true
            medicoRepository.save(medicoLogado) // Atualiza o médico como ativo no repositório
            ResponseEntity.ok(medicoLogado)
        } else {
            ResponseEntity.status(401).build()
        }
    }

    @CrossOrigin
    @PutMapping("/logout")
    fun deslogarMedico(@RequestBody medico: Medico): ResponseEntity<Medico> {
        val medicoLogado = medicoRepository.findByEmail(medico.email ?: "")

        return if (medicoLogado != null && medicoLogado.ativo) {
            // Marca o médico como inativo
            medicoLogado.ativo = false
            medicoRepository.save(medicoLogado) // Atualiza o médico como inativo no repositório
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(401).build()
        }
    }



    @CrossOrigin
    @PostMapping
    fun adicionarMedico(@RequestBody @Valid novoMedico: Medico): ResponseEntity<Medico> {
        medicoService.salvar(novoMedico)
        return ResponseEntity.status(201).body(novoMedico)
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarMedico(@PathVariable id: Int, @RequestBody @Valid novoMedico: Medico): ResponseEntity<*>{
            val medicoAtualizado = medicoService.atualizar(id, novoMedico)
            return ResponseEntity.ok(medicoAtualizado)

    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarMedico(@PathVariable id: Int): ResponseEntity<Medico> {
            medicoService.deletar(id)
            return ResponseEntity.status(200).build()

    }

    @CrossOrigin
    @GetMapping
    fun listarMedicos(): ResponseEntity<List<Medico>> {
        val medicos = medicoService.getLista()
        return ResponseEntity.status(200).body(medicos)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun listarMedicoPorId(@PathVariable id: Int): ResponseEntity<Medico> {
        val medico = medicoService.listarPorId(id)
        return ResponseEntity.status(200).body(medico)
    }

    @CrossOrigin
    @GetMapping("/totalAdministradores")
    fun totalAdministradores(): ResponseEntity<Long> {
        val total = medicoService.totalAdministradores()
        return ResponseEntity.status(200).body(total)
    }

    @CrossOrigin
    @GetMapping("/totalAdministradoresAtivos")
    fun totalAdministradoresAtivos(): ResponseEntity<Long> {
        val total = medicoService.totalAdministradoresAtivos()
        return ResponseEntity.status(200).body(total)
    }

    @CrossOrigin
    @GetMapping("/graficoGeral")
    fun obterDadosGraficoGeral(): ResponseEntity<List<GraficoGeralAdm>> {
        val dadosGrafico = medicoService.obterDadosGraficoGeral()
        return ResponseEntity.ok(dadosGrafico)
    }

}