package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.PacienteService
import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dominio.Paciente
import Multiclinics.SpringV2.dto.PacienteComResponsavel
import Multiclinics.SpringV2.dto.PacienteMedicoDto
import Multiclinics.SpringV2.dto.PacienteSemResponsavel
import Multiclinics.SpringV2.repository.PacienteRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pacientes")
class PacienteController(
    val pacienteRepository: PacienteRepository,
    val pacienteService: PacienteService
) {

    @PostMapping("/ComResponsavel")
    fun adicionarPaciente(@RequestBody novoPaciente: PacienteComResponsavel): ResponseEntity<Paciente> {
        val pacienteCriado = pacienteService.salvar(novoPaciente)
        return ResponseEntity.status(201).body(pacienteCriado)
    }


    @PostMapping("/SemResponsavel")
    fun adicionarPacienteSemResponsavel(@RequestBody novoPaciente: PacienteSemResponsavel): ResponseEntity<Paciente> {
        val pacienteCriado = pacienteService.salvarSemResponsavel(novoPaciente)
        return ResponseEntity.status(201).body(pacienteCriado)
    }


    @PutMapping("/{id}")
    fun atualizarPaciente(@PathVariable id: Int, @RequestBody @Valid novoPaciente: Paciente): ResponseEntity<*> {

        val pacienteAtualizado = pacienteService.atualizar(id, novoPaciente)
        return ResponseEntity.ok(pacienteAtualizado)
    }


    @DeleteMapping("/{id}")
    fun deletarPaciente(@PathVariable id: Int): ResponseEntity<Paciente> {
        pacienteService.deletar(id)
        return ResponseEntity.status(200).build()
    }


    @GetMapping
    fun listarPaciente(): ResponseEntity<List<Paciente>> {
        val pacientes = pacienteService.getLista()
        return ResponseEntity.status(200).body(pacientes)
    }


    @GetMapping("/{id}")
    fun buscarPacientePorId(@PathVariable id: Int): ResponseEntity<Paciente> {
        return pacienteService.buscarPacientePorId(id)
    }


    @GetMapping("/conversoes-ultimos-seis-meses")
    fun getConversoesUltimosSeisMeses(): ResponseEntity<List<Map<String, Any>>> {
        val conversoes = pacienteService.getConversoesUltimosSeisMeses()
        return ResponseEntity.ok(conversoes)
    }



    @GetMapping("/porcentagem-aba")
    fun calcularPorcentagemPacientesABA(): ResponseEntity<Double> {
        val porcentagemABA = pacienteService.calcularPorcentagemPacientesABA()
        return ResponseEntity.ok(porcentagemABA)
    }


    @GetMapping("/ativos")
    fun contarPacientesAtivos(): ResponseEntity<Long> {
        val pacientesAtivos = pacienteService.contarPacientesAtivos()
        return ResponseEntity.ok(pacientesAtivos)
    }


    @GetMapping("/ultimo-trimestre")
    fun contarPacientesUltimoTrimestre(): ResponseEntity<Long> {
        val pacientesUltimoTrimestre = pacienteService.contarPacientesUltimoTrimestre()
        return ResponseEntity.ok(pacientesUltimoTrimestre)
    }


    @GetMapping("/agendamentos-vencidos")
    fun contarAgendamentosVencidos(): ResponseEntity<Long> {
        val agendamentosVencidos = pacienteService.contarAgendamentosVencidos()
        return ResponseEntity.ok(agendamentosVencidos)
    }

}