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


    @PostMapping("/login")
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




    @PostMapping
    fun adicionarMedico(@RequestBody @Valid novoMedico: Medico): ResponseEntity<Medico> {
        medicoService.salvar(novoMedico)
        return ResponseEntity.status(201).body(novoMedico)
    }


    @PutMapping("/{id}")
    fun atualizarMedico(@PathVariable id: Int, @RequestBody @Valid novoMedico: Medico): ResponseEntity<*>{
            val medicoAtualizado = medicoService.atualizar(id, novoMedico)
            return ResponseEntity.ok(medicoAtualizado)

    }


    @DeleteMapping("/{id}")
    fun deletarMedico(@PathVariable id: Int): ResponseEntity<Medico> {
            medicoService.deletar(id)
            return ResponseEntity.status(200).build()

    }


    @GetMapping
    fun listarMedicos(): ResponseEntity<List<Medico>> {
        val medicos = medicoService.getLista()
        return ResponseEntity.status(200).body(medicos)
    }


    @GetMapping("/todos")
    fun listarTodosMedicos(): ResponseEntity<List<Medico>> {
        val medicos = medicoService.getListaTodos()
        return ResponseEntity.status(200).body(medicos)
    }


    @GetMapping("/{id}")
    fun listarMedicoPorId(@PathVariable id: Int): ResponseEntity<Medico> {
        val medico = medicoService.listarPorId(id)
        return ResponseEntity.status(200).body(medico)
    }


    @GetMapping("/totalAdministradores")
    fun totalAdministradores(): ResponseEntity<Long> {
        val total = medicoService.totalAdministradores()
        return ResponseEntity.status(200).body(total)
    }


    @GetMapping("/totalAdministradoresAtivos")
    fun totalAdministradoresAtivos(): ResponseEntity<Long> {
        val total = medicoService.totalAdministradoresAtivos()
        return ResponseEntity.status(200).body(total)
    }


    @GetMapping("/graficoGeral")
    fun obterDadosGraficoGeral(): ResponseEntity<List<GraficoGeralAdm>> {
        val dadosGrafico = medicoService.obterDadosGraficoGeral()
        return ResponseEntity.ok(dadosGrafico)
    }

    @PatchMapping("/{id}/inativar")
    fun inativarMedico(@PathVariable id: Int): ResponseEntity<Any> {
        medicoService.inativarMedico(id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/ativar")
    fun ativarMedico(@PathVariable id: Int): ResponseEntity<Any> {
        medicoService.ativarMedico(id)
        return ResponseEntity.ok().build()
    }

}