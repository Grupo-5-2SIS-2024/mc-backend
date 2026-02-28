package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dominio.StatusConsulta
import Multiclinics.SpringV2.repository.ConsultaRepository
import Multiclinics.SpringV2.repository.MedicoRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import Multiclinics.SpringV2.dominio.DiaSemana
import Multiclinics.SpringV2.repository.CargaHorariaRepository
import java.time.LocalTime

@Service
class ConsultaService(
    val consultaRepository: ConsultaRepository,
    val medicoRepository: MedicoRepository,
    private val statusConsultaService: StatusConsultaService, // Adiciona a dependência
    private val cargaHorariaRepository: CargaHorariaRepository
) {
    fun validarLista(lista: List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun atualizar(id: Int, novaConsulta: Consulta): ResponseEntity<Consulta> {
        if (!medicoRepository.existsById(novaConsulta.medico!!.id!!)) {
            return ResponseEntity.status(404).build()
        }

        val consultaExistente = consultaRepository.findById(id)
        if (consultaExistente.isPresent) {
            val consultaEscolhida = consultaExistente.get()

            // Atualiza os dados da consulta existente com os novos dados
            val consultaAtualizada = consultaEscolhida.copy(
                datahoraConsulta = novaConsulta.datahoraConsulta,
                descricao = novaConsulta.descricao,
                medico = novaConsulta.medico,
                especificacaoMedica = novaConsulta.especificacaoMedica,
                statusConsulta = novaConsulta.statusConsulta,
                paciente = novaConsulta.paciente,
                duracaoConsulta = novaConsulta.duracaoConsulta
            )

            val consultaAlterada = consultaRepository.save(consultaAtualizada)
            return ResponseEntity.status(200).body(consultaAlterada)
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    fun salvar(novaConsulta: Consulta): Consulta {
        val medicoId = novaConsulta.medico?.id ?: throw ResponseStatusException(HttpStatusCode.valueOf(400), "Médico obrigatório")
        val pacienteId = novaConsulta.paciente?.id ?: throw ResponseStatusException(HttpStatusCode.valueOf(400), "Paciente obrigatório")
        val dt = novaConsulta.datahoraConsulta ?: throw ResponseStatusException(HttpStatusCode.valueOf(400), "Data/hora obrigatória")

        val durMin = duracaoEmMinutos(novaConsulta.duracaoConsulta)
        if (durMin !in listOf(30, 50, 60)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(400), "Duração inválida")
        }

        val data = dt.toLocalDate()
        val hora = dt.toLocalTime().toString().substring(0, 5)

        val disponiveis = listarHorariosDisponiveis(medicoId, data, durMin, pacienteId)
        if (!disponiveis.contains(hora)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(409), "Horário indisponível para o profissional ou paciente")
        }

        return consultaRepository.save(novaConsulta)
    }

    fun deletar(id: Int): ResponseEntity<Any> {
        val consultaExistente = consultaRepository.findById(id)
        return if (consultaExistente.isPresent) {
            val consultaEscolhida = consultaExistente.get()
            consultaRepository.delete(consultaEscolhida)
            ResponseEntity.ok("Consulta cancelada com sucesso.")
        } else {
            ResponseEntity.status(404).build()
        }
    }

    fun getTop3ConsultasByData(): List<Map<String, Any?>> {
        val consultas = consultaRepository.findTop3ByOrderByDatahoraConsultaDesc()
        return consultas.map { array ->
            mapOf(
                "nomePaciente" to array[0],
                "dataConsulta" to array[1],
                "especialidadeMedico" to array[2],
                "details" to mapOf(
                    "pacienteId" to (array.getOrNull(3)?.toString() ?: "null"),
                    "medicoId" to (array.getOrNull(4)?.toString() ?: "null"),
                    "especMedicaId" to (array.getOrNull(5)?.toString() ?: "null"),
                    "genero" to (array.getOrNull(6)?.toString() ?: "null"),
                    "statusConsultaId" to (array.getOrNull(7)?.toString() ?: "null")
                )
            )
        }
    }




    fun getLista(): List<Consulta> {
        val lista = consultaRepository.findAll()
        validarLista(lista)

        return lista
    }

    fun getListaNome(nome: String): ResponseEntity<List<Consulta>> {
        val consultasMedico = consultaRepository.findByMedicoNome(nome)
        return ResponseEntity.status(200).body(consultasMedico)
    }

    fun getListaID(id: Int): ResponseEntity<List<Consulta>> {
        val consultasMedico = consultaRepository.findByMedicoId(id)
        return ResponseEntity.status(200).body(consultasMedico)
    }


    // API INDIVIDUAL PEDRO



    fun getAltasUltimosSeisMeses(): List<Map<String, Any>> {
        val result = consultaRepository.findAltasUltimosSeisMeses()

        // Agrupando os detalhes por ano e mês
        val groupedResult = result.groupBy { Pair(it[0], it[1]) } // Agrupa por ano e mês

        return groupedResult.map { (key, records) ->
            val (ano, mes) = key
            mapOf(
                "ano" to ano,
                "mes" to mes,
                "total" to records.sumOf { (it[2] as Long) }, // Soma o total
                "details" to records.map {
                    mapOf(
                        "pacienteId" to (it[3]?.toString() ?: "null"),
                        "medicoId" to (it[4]?.toString() ?: "null"),
                        "especMedicaId" to (it[5]?.toString() ?: "null"),
                        "genero" to (it[6]?.toString() ?: "null"),
                        "statusConsultaId" to (it[7]?.toString() ?: "null")
                    )
                }
            )
        }
    }



    fun getHorariosUltimosSeisMeses(): List<Map<String, Any>> {
        val result = consultaRepository.findHorariosUltimosSeisMeses()

        // Agrupando os registros por ano e mês
        val groupedResult = result.groupBy { Pair(it[0], it[1]) } // Agrupa por ano e mês

        return groupedResult.map { (key, records) ->
            val (ano, mes) = key
            mapOf(
                "ano" to ano,
                "mes" to mes,
                "agendados" to records.sumOf { it[2] as Long }, // Soma os valores de agendados
                "disponiveis" to records.first()[3], // disponiveis será o mesmo para o grupo
                "details" to records.map {
                    mapOf(
                        "pacienteId" to (it.getOrNull(4)?.toString() ?: "null"),
                        "medicoId" to (it.getOrNull(5)?.toString() ?: "null"),
                        "especMedicaId" to (it.getOrNull(6)?.toString() ?: "null"),
                        "genero" to (it.getOrNull(7)?.toString() ?: "null"),
                        "statusConsultaId" to (it.getOrNull(8)?.toString() ?: "null")
                    )
                }
            )
        }
    }



    fun getConcluidosETotal(): Map<String, Any> {
        val concluidos = consultaRepository.countConcluidos()
        val detailsList = concluidos.map { record ->
            mapOf(
                "pacienteID" to (record[2]?.toString() ?: "null"),
                "medicoID" to (record[3]?.toString() ?: "null"),
                "especMedicaID" to (record[4]?.toString() ?: "null"),
                "genero" to (record[5]?.toString() ?: "null"),
                "statusConsultaID" to (record[6]?.toString() ?: "null")
            )
        }
        val realizadas = concluidos.sumOf { (it[0] as Double) }
        val total = concluidos.sumOf { (it[1] as Double) }

        return mapOf(
            "realizadas" to realizadas,
            "total" to total,
            "details" to detailsList
        )
    }


    fun getPercentagemConcluidos3(): Double {
        val cancelados = consultaRepository.countCancelada()

        return cancelados.toDouble()
    }

    fun atualizarStatus(id: Int, statusId: Int): Consulta {
        val consultaExistente = consultaRepository.findById(id)
        if (consultaExistente.isPresent) {
            val consulta = consultaExistente.get()
            val consultaAtualizada = consulta.copy(statusConsulta = consulta.statusConsulta?.copy(id = statusId))
            return consultaRepository.save(consultaAtualizada)
        } else {
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    fun atualizarStatusComStatus(id: Int, status: StatusConsulta): Consulta {
        val consultaExistente = consultaRepository.findById(id)
        if (consultaExistente.isPresent) {
            val consulta = consultaExistente.get()
            val consultaAtualizada = consulta.copy(statusConsulta = status)
            return consultaRepository.save(consultaAtualizada)
        } else {
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
        }
    }

    fun atualizarStatusConsulta(id: Int, statusId: Int): Consulta? {
        val consultaOpt = consultaRepository.findById(id)
        if (consultaOpt.isPresent) {
            val consulta = consultaOpt.get()
            // Busca o status pelo id
            val status = statusConsultaService.buscarPorId(statusId)
            if (status != null) {
                consulta.statusConsulta = status
                consultaRepository.save(consulta)
                return consulta
            }
        }
        return null
    }

    private val SLOTS_ABA = listOf(
        "08:00","08:50","09:40","10:30","11:00","11:50","12:40",
        "13:40","14:30","15:20","16:10","17:00"
    )

    private val SLOTS_CONV = listOf(
        "08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00",
        "13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30"
    )

    private val SLOTS_NEURO = listOf(
        "08:00","09:00","10:00","11:00","12:00",
        "13:00","14:00","15:00","16:00","17:00"
    )

    private fun diaSemanaEnum(data: LocalDate): DiaSemana {
        return when (data.dayOfWeek) {
            java.time.DayOfWeek.MONDAY -> DiaSemana.SEGUNDA
            java.time.DayOfWeek.TUESDAY -> DiaSemana.TERCA
            java.time.DayOfWeek.WEDNESDAY -> DiaSemana.QUARTA
            java.time.DayOfWeek.THURSDAY -> DiaSemana.QUINTA
            java.time.DayOfWeek.FRIDAY -> DiaSemana.SEXTA
            java.time.DayOfWeek.SATURDAY -> DiaSemana.SABADO
            java.time.DayOfWeek.SUNDAY -> DiaSemana.DOMINGO
        }
    }

    private fun overlaps(aIni: LocalDateTime, aFim: LocalDateTime, bIni: LocalDateTime, bFim: LocalDateTime): Boolean {
        return aIni.isBefore(bFim) && aFim.isAfter(bIni)
    }

    private fun duracaoEmMinutos(t: LocalTime?): Int {
        if (t == null) return 0
        return t.hour * 60 + t.minute
    }

    private fun slotsPorDuracao(duracaoMin: Int): List<String> {
        return when (duracaoMin) {
            50 -> SLOTS_ABA
            30 -> SLOTS_CONV
            60 -> SLOTS_NEURO
            else -> emptyList()
        }
    }

    private fun parseHora(hhmm: String): LocalTime {
        val p = hhmm.split(":")
        return LocalTime.of(p[0].toInt(), p[1].toInt())
    }

    fun listarHorariosDisponiveis(medicoId: Int, data: LocalDate, duracaoMin: Int, pacienteId: Int?): List<String> {
        if (!medicoRepository.existsById(medicoId)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(404), "Médico não encontrado")
        }

        val slotsBase = slotsPorDuracao(duracaoMin)
        if (slotsBase.isEmpty()) return emptyList()

        val diaEnum = diaSemanaEnum(data)

        val cargasDoDia = cargaHorariaRepository.findByMedicoId(medicoId)
            .filter { it.diaSemana == diaEnum }

        if (cargasDoDia.isEmpty()) return emptyList()

        val iniDia = data.atStartOfDay()
        val fimDia = data.plusDays(1).atStartOfDay()

        val consultasMedico = consultaRepository.findAtivasDoMedicoNoDia(medicoId, iniDia, fimDia)
        val consultasPaciente = if (pacienteId != null) {
            consultaRepository.findAtivasDoPacienteNoDia(pacienteId, iniDia, fimDia)
        } else {
            emptyList()
        }

        val bloqueios = (consultasMedico + consultasPaciente).distinctBy { it.id }

        fun cabeNaCarga(inicio: LocalTime, fim: LocalTime): Boolean {
            return cargasDoDia.any { c ->
                (inicio >= c.horaInicio) && (fim <= c.horaFim)
            }
        }

        val disponiveis = mutableListOf<String>()

        for (slot in slotsBase) {
            val inicio = parseHora(slot)
            val fim = inicio.plusMinutes(duracaoMin.toLong())

            if (!cabeNaCarga(inicio, fim)) continue

            val iniDT = LocalDateTime.of(data, inicio)
            val fimDT = LocalDateTime.of(data, fim)

            val colide = bloqueios.any { b ->
                val bIni = b.datahoraConsulta ?: return@any false
                val bFim = bIni.plusMinutes(duracaoEmMinutos(b.duracaoConsulta).toLong())
                overlaps(iniDT, fimDT, bIni, bFim)
            }

            if (!colide) disponiveis.add(slot)
        }

        return disponiveis
    }

}
