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
    fun listarPaciente(@ModelAttribute filtro: Multiclinics.SpringV2.dto.PacienteFiltroRequest): ResponseEntity<Any> {
        return if (filtro.page != null && filtro.size != null) {
            val pacientes = pacienteService.listarPaginadoComFiltro(filtro)
            ResponseEntity.ok(pacientes)
        } else {
            val pacientes = pacienteService.getLista()
            ResponseEntity.status(200).body(pacientes)
        }
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



    @PatchMapping("/{id}/inativar")
    fun inativarPaciente(@PathVariable id: Int): ResponseEntity<Any> {
        pacienteService.inativarPaciente(id)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{id}/ativar")
    fun ativarPaciente(@PathVariable id: Int): ResponseEntity<Any> {
        pacienteService.ativarPaciente(id)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{pacienteId}/responsavel/{responsavelId}")
    fun vincularResponsavel(
        @PathVariable pacienteId: Int,
        @PathVariable responsavelId: Int
    ): ResponseEntity<Void> {
        pacienteService.vincularResponsavel(pacienteId, responsavelId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/por-responsavel")
    fun listarPorNomeResponsavel(@RequestParam nome: String): ResponseEntity<List<Paciente>> {
        val lista = pacienteRepository.findPacientesByNomeResponsavel(nome)
        return ResponseEntity.ok(lista)
    }

}