package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.Service.ConsultaService
import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.repository.ConsultaRepository
import Multiclinics.SpringV2.repository.MedicoRepository
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType



@RestController
@RequestMapping("/consultas")
class ConsultaController(
    val consultaRepository: ConsultaRepository,
    val medicoRepository: MedicoRepository,
    val consultaService: ConsultaService
) {

    @CrossOrigin
    @PostMapping
    fun agendarConsulta(@RequestBody @Valid novaConsulta: Consulta): ResponseEntity<Consulta> {

        val consultaAgendada = consultaService.salvar(novaConsulta)
        return ResponseEntity.status(201).body(consultaAgendada)
    }

    @CrossOrigin
    @PutMapping("/{id}")
    fun alterarConsulta(@PathVariable id: Int, @RequestBody @Valid novaConsulta: Consulta): ResponseEntity<*> {
        val consultaAtualizada = consultaService.atualizar(id, novaConsulta)
        return ResponseEntity.ok(consultaAtualizada)
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    fun cancelarConsulta(@PathVariable id: Int): ResponseEntity<Consulta> {
        consultaService.deletar(id)
        return  ResponseEntity.status(200).build()
    }
    @CrossOrigin
    @GetMapping("/agendamentosProximos")
    fun agendamentosProximos(): ResponseEntity<*> {
        val agendamentoProximos = consultaService.getTop3ConsultasByData()
        return ResponseEntity.ok(agendamentoProximos)
    }


    @CrossOrigin
    @GetMapping
    fun listar(): ResponseEntity<List<Consulta>> {
        val Consultas = consultaService.getLista()

        return ResponseEntity.status(200).body(Consultas)

    }

    @CrossOrigin
    @GetMapping("/{nome}")
    fun listarConsultasMedico(@PathVariable nome: String): ResponseEntity<*> {
        val consultasMedico = consultaService.getListaNome(nome)
        return ResponseEntity.status(200).body(consultasMedico)
    }

    @CrossOrigin
    @GetMapping("/listarConsultasMedicoID/{id}")
    fun listarConsultasMedicoID(@PathVariable id: Int): ResponseEntity<*> {
        val consultasMedico = consultaService.getListaID(id)
        return ResponseEntity.status(200).body(consultasMedico)
    }




// API INDIVIDUAL PEDRI

    @CrossOrigin
    @GetMapping("/altas-ultimos-seis-meses")
    fun getAltasUltimosSeisMeses(): ResponseEntity<List<Map<String, Any>>> {
        val altas = consultaService.getAltasUltimosSeisMeses()
        return ResponseEntity.ok(altas)
    }

    @CrossOrigin
    @GetMapping("/horarios-ultimos-seis-meses")
    fun getHorariosUltimosSeisMeses(): ResponseEntity<List<Map<String, Any>>> {
        val horarios = consultaService.getHorariosUltimosSeisMeses()
        return ResponseEntity.ok(horarios)
    }



    @CrossOrigin
    @GetMapping("/percentagem-concluidos")
    fun getConcluidosETotal(): Map<String, Any> {
        return consultaService.getConcluidosETotal()
    }








    @CrossOrigin
    @GetMapping("/export/csv")
    fun exportarConsultasCsv(response: HttpServletResponse) {
        val consultas = consultaService.getLista()


        response.contentType = "text/csv"
        response.setHeader("Content-Disposition", "attachment; filename=consultas.csv")


        val writer = response.writer
        writer.append("ID,Data e Hora,Descrição,Médico,Paciente,Status,Especificação Médica\n")

        consultas.forEach { consulta ->
            writer.append(
                "${consulta.id}," +
                        "${consulta.datahoraConsulta}," +
                        "\"${consulta.descricao}\"," +
                        "\"${consulta.medico?.nome}\"," +
                        "\"${consulta.paciente?.nome}\"," +
                        "\"${consulta.statusConsulta?.nomeStatus}\"," +
                        "\"${consulta.especificacaoMedica?.area}\"\n"
            )
        }

        writer.flush()
        writer.close()
    }



}