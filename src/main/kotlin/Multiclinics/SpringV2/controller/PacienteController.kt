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
    @CrossOrigin
    @PostMapping("/ComResponsavel")
    fun adicionarPaciente(@RequestBody novoPaciente: PacienteComResponsavel): ResponseEntity<Paciente> {
        val pacienteCriado = pacienteService.salvar(novoPaciente)
        return ResponseEntity.status(201).body(pacienteCriado)
    }

    @CrossOrigin
    @PostMapping("/SemResponsavel")
    fun adicionarPacienteSemResponsavel(@RequestBody novoPaciente: PacienteSemResponsavel): ResponseEntity<Paciente> {
        val pacienteCriado = pacienteService.salvarSemResponsavel(novoPaciente)
        return ResponseEntity.status(201).body(pacienteCriado)
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun atualizarPaciente(@PathVariable id: Int, @RequestBody @Valid novoPaciente: Paciente): ResponseEntity<*> {

        val pacienteAtualizado = pacienteService.atualizar(id, novoPaciente)
        return ResponseEntity.ok(pacienteAtualizado)
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun deletarPaciente(@PathVariable id: Int): ResponseEntity<Paciente> {
        pacienteService.deletar(id)
        return ResponseEntity.status(200).build()
    }

    @CrossOrigin
    @GetMapping
    fun listarPaciente(): ResponseEntity<List<Paciente>> {
        val pacientes = pacienteService.getLista()
        return ResponseEntity.status(200).body(pacientes)
    }

    @CrossOrigin
    @GetMapping("/{id}")
    fun buscarPacientePorId(@PathVariable id: Int): ResponseEntity<Paciente> {
        return pacienteService.buscarPacientePorId(id)
    }

    @CrossOrigin
    @GetMapping("/conversoes-ultimos-seis-meses")
    fun getConversoesUltimosSeisMeses(): ResponseEntity<List<Map<String, Any>>> {
        val conversoes = pacienteService.getConversoesUltimosSeisMeses()
        return ResponseEntity.ok(conversoes)
    }


    @CrossOrigin
    @GetMapping("/porcentagem-aba")
    fun calcularPorcentagemPacientesABA(): ResponseEntity<Double> {
        val porcentagemABA = pacienteService.calcularPorcentagemPacientesABA()
        return ResponseEntity.ok(porcentagemABA)
    }

    @CrossOrigin
    @GetMapping("/ativos")
    fun contarPacientesAtivos(): ResponseEntity<Long> {
        val pacientesAtivos = pacienteService.contarPacientesAtivos()
        return ResponseEntity.ok(pacientesAtivos)
    }

    @CrossOrigin
    @GetMapping("/ultimo-trimestre")
    fun contarPacientesUltimoTrimestre(): ResponseEntity<Long> {
        val pacientesUltimoTrimestre = pacienteService.contarPacientesUltimoTrimestre()
        return ResponseEntity.ok(pacientesUltimoTrimestre)
    }

    @CrossOrigin
    @GetMapping("/agendamentos-vencidos")
    fun contarAgendamentosVencidos(): ResponseEntity<Long> {
        val agendamentosVencidos = pacienteService.contarAgendamentosVencidos()
        return ResponseEntity.ok(agendamentosVencidos)
    }

}