package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.ConsultaService
import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.repository.ConsultaRepository
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import Multiclinics.SpringV2.dto.ConsultaRecorrenteRequest
import Multiclinics.SpringV2.dto.ConsultaRecorrenteResponse
import java.time.LocalTime
import java.util.Map
import java.util.HashMap




@RestController
@RequestMapping("/consultas")
class ConsultaController(
    val consultaRepository: ConsultaRepository,
    val consultaService: ConsultaService,
) {


    @PostMapping
    fun agendarConsulta(@RequestBody @Valid novaConsulta: Consulta): ResponseEntity<Consulta> {

        val consultaAgendada = consultaService.salvar(novaConsulta)
        return ResponseEntity.status(201).body(consultaAgendada)
    }


    @PutMapping("/{id}")
    fun alterarConsulta(@PathVariable id: Int, @RequestBody @Valid novaConsulta: Consulta): ResponseEntity<Consulta> {
        val consultaAtualizada = consultaService.atualizar(id, novaConsulta)
        return ResponseEntity.ok(consultaAtualizada)
    }


    @DeleteMapping("/{id}")
    fun cancelarConsulta(@PathVariable id: Int): ResponseEntity<Consulta> {
        consultaService.deletar(id)
        return  ResponseEntity.status(200).build()
    }

    @GetMapping("/agendamentosProximos")
    fun agendamentosProximos(): ResponseEntity<*> {
        val agendamentoProximos = consultaService.getTop3ConsultasByData()
        return ResponseEntity.ok(agendamentoProximos)
    }



    @GetMapping
    fun listar(): ResponseEntity<List<Consulta>> {
        val Consultas = consultaService.getLista()

        return ResponseEntity.status(200).body(Consultas)

    }


    @GetMapping("/{nome}")
    fun listarConsultasMedico(@PathVariable nome: String): ResponseEntity<*> {
        val consultasMedico = consultaService.getListaNome(nome)
        return ResponseEntity.status(200).body(consultasMedico)
    }


    @GetMapping("/listarConsultasMedicoID/{id}")
    fun listarConsultasMedicoID(@PathVariable id: Int): ResponseEntity<*> {
        val consultasMedico = consultaService.getListaID(id)
        return ResponseEntity.status(200).body(consultasMedico)
    }




// API INDIVIDUAL PEDRI


    @GetMapping("/altas-ultimos-seis-meses")
    fun getAltasUltimosSeisMeses(): ResponseEntity<List<kotlin.collections.Map<String, Any>>> {
        val altas = consultaService.getAltasUltimosSeisMeses()
        return ResponseEntity.ok(altas)
    }


    @GetMapping("/horarios-ultimos-seis-meses")
    fun getHorariosUltimosSeisMeses(): ResponseEntity<List<kotlin.collections.Map<String, Any>>> {
        val horarios = consultaService.getHorariosUltimosSeisMeses()
        return ResponseEntity.ok(horarios)
    }




    @GetMapping("/percentagem-concluidos")
    fun getConcluidosETotal(): kotlin.collections.Map<String, Any> {
        return consultaService.getConcluidosETotal()
    }



    @GetMapping("/id/{id}")
    fun obterConsultaPorId(@PathVariable id: Int): ResponseEntity<Consulta> {
        val consulta = consultaRepository.findById(id)
        return if (consulta.isPresent) {
            ResponseEntity.ok(consulta.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }




    @GetMapping("/export/csv")
    fun exportarConsultasCsv(response: HttpServletResponse) {
        val consultas = consultaService.getLista()


        response.contentType = "text/csv"
        response.setHeader("Content-Disposition", "attachment; filename=consultas.csv")


        val writer = response.writer
        writer.append("ID,Data e Hora,Descrição,Médico,Paciente,Status,Especificação Médica,Sala\n")

        consultas.forEach { consulta ->
            writer.append(
                "${consulta.id}," +
                        "${consulta.datahoraConsulta}," +
                        "\"${consulta.descricao}\"," +
                        "\"${consulta.medico?.nome}\"," +
                        "\"${consulta.paciente?.nome}\"," +
                        "\"${consulta.statusConsulta?.nomeStatus}\"," +
                        "\"${consulta.especificacaoMedica?.area}\"," +
                        "\"${consulta.sala?.nome ?: ""}\"\n"
            )
        }

        writer.flush()
        writer.close()
    }

    @PatchMapping("/{id}/status")
    fun atualizarStatusConsulta(@PathVariable id: Int, @RequestParam statusId: Int): ResponseEntity<Consulta> {
        val consultaAtualizada = consultaService.atualizarStatusConsulta(id, statusId)
        return ResponseEntity.ok(consultaAtualizada)
    }

    @GetMapping("/semana")
    fun listarConsultasSemana(
        @RequestParam inicio: String
    ): ResponseEntity<List<Consulta>> {
        // Espera-se o formato yyyy-MM-dd
        val dataInicio = java.time.LocalDate.parse(inicio)
        val dataFim = dataInicio.plusDays(6)
        val consultasSemana = consultaService.buscarPorIntervalo(dataInicio, dataFim)
        return ResponseEntity.ok(consultasSemana)
    }



    @GetMapping("/disponiveis")
    fun horariosDisponiveis(
        @RequestParam medicoId: Int,
        @RequestParam data: String,
        @RequestParam duracaoMin: Int,
        @RequestParam(required = false) pacienteId: Int?
    ): ResponseEntity<kotlin.collections.Map<String, List<String>>> {
        val d = LocalDate.parse(data)
        val lista = consultaService.listarHorariosDisponiveis(medicoId, d, duracaoMin, pacienteId)
        return ResponseEntity.ok(mapOf("horarios" to lista))
    }

    @PostMapping("/recorrentes")
    fun agendarRecorrentes(@RequestBody @Valid req: ConsultaRecorrenteRequest): ResponseEntity<ConsultaRecorrenteResponse> {
        val resp = consultaService.salvarRecorrentes(req)
        return ResponseEntity.status(201).body(resp)
    }

    @GetMapping("/painel-dia")
    fun listarPainelDoDia(
        @RequestParam(required = false) data: String?,
        @RequestParam(required = false) medico: String?,
        @RequestParam(required = false) duracao: Int?
    ): ResponseEntity<List<kotlin.collections.Map<String, Any?>>> {
        val dataDia = if (data.isNullOrBlank()) null else LocalDate.parse(data)
        val resultado = consultaService.listarPainelDoDia(dataDia, medico, duracao)
        return ResponseEntity.ok(resultado)
    }

    @GetMapping("/salas-disponiveis")
    fun salasDisponiveis(
        @RequestParam data: String,
        @RequestParam hora: String,
        @RequestParam duracaoMin: Int
    ): ResponseEntity<kotlin.collections.Map<String, Any>> {
        val dataConsulta = LocalDate.parse(data)
        val horaConsulta = LocalTime.parse(hora)
        val lista = consultaService.listarSalasDisponiveis(dataConsulta, horaConsulta, duracaoMin)
        val response = mapOf("salas" to lista)
        return ResponseEntity.ok(response)
    }


}