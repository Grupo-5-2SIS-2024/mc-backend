package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.dominio.Medico
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

@Service
class ConsultaService(
    val consultaRepository: ConsultaRepository,
    val medicoRepository: MedicoRepository
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
        if (!medicoRepository.existsById(novaConsulta.medico!!.id!!)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(404))
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




}
