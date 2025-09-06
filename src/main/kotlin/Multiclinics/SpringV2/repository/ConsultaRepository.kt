package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Consulta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime

interface ConsultaRepository : JpaRepository<Consulta, Int> {
    fun findByMedicoNome(nome: String): List<Consulta>
    fun findByMedicoId(id: Int): List<Consulta>

    @Query("""
    SELECT 
        p.nome AS nomePaciente, 
        c.datahoraConsulta AS dataConsulta, 
        c.especificacaoMedica.area AS especialidadeMedico,
        p.id AS pacienteId,
        c.medico.id AS medicoId,
        c.medico.especificacaoMedica.id AS especMedicaId,
        p.genero AS genero,
        c.statusConsulta.id AS statusConsultaId
    FROM 
        Consulta c 
    JOIN 
        c.paciente p
    ORDER BY 
        c.datahoraConsulta DESC
    LIMIT 3
""")
    fun findTop3ByOrderByDatahoraConsultaDesc(): List<Array<Any>>


    @Query("SELECT COUNT(c) FROM Consulta c")
    fun countTotal(): Long

// API INDIVIDUAL PEDRO

    @Query("""
    SELECT 
        YEAR(c.paciente.dtSaida) AS ano,
        MONTH(c.paciente.dtSaida) AS mes,
        COUNT(c) AS total,
        c.paciente.id AS pacienteId,
        c.medico.id AS medicoId,
        c.medico.especificacaoMedica.id AS especMedicaId,
        c.paciente.genero AS genero,
        c.statusConsulta.id AS statusConsultaId
    FROM 
        Consulta c
    WHERE 
        c.paciente.dtSaida >= DATEADD(MONTH, -6, CURRENT_DATE)
    GROUP BY 
        YEAR(c.paciente.dtSaida), MONTH(c.paciente.dtSaida), 
        c.paciente.id, c.medico.id, c.medico.especificacaoMedica.id, 
        c.paciente.genero, c.statusConsulta.id
    ORDER BY 
        ano, mes
""")
    fun findAltasUltimosSeisMeses(): List<Array<Any>>



    @Query("""
    SELECT 
        YEAR(c.datahoraConsulta) AS ano,
        MONTH(c.datahoraConsulta) AS mes,
        COUNT(*) AS agendados,
        (300 - COUNT(*)) AS disponiveis,
        c.paciente.id AS pacienteId,
        c.medico.id AS medicoId,
        c.medico.especificacaoMedica.id AS especMedicaId,
        c.paciente.genero AS genero,
        c.statusConsulta.id AS statusConsultaId
    FROM 
        Consulta c
    WHERE 
        c.datahoraConsulta >= DATEADD(MONTH, -6, CURRENT_DATE)
    GROUP BY 
        YEAR(c.datahoraConsulta), MONTH(c.datahoraConsulta), 
        c.paciente.id, c.medico.id, c.medico.especificacaoMedica.id, 
        c.paciente.genero, c.statusConsulta.id
    ORDER BY 
        ano, mes
""")
    fun findHorariosUltimosSeisMeses(): List<Array<Any>>











    @Query("""
    SELECT 
        CAST(COUNT(CASE WHEN c.statusConsulta.nomeStatus = 'Realizada' THEN 1 END) AS DOUBLE) AS realizadas,
        CAST(COUNT(CASE WHEN c.statusConsulta.nomeStatus != 'Realizada' THEN 1 END) AS DOUBLE) AS total,
        c.paciente.id AS pacienteId,
        c.medico.id AS medicoId,
        c.medico.especificacaoMedica.id AS especMedicaId,
        c.paciente.genero AS genero,
        c.statusConsulta.id AS statusConsultaId
    FROM Consulta c
    GROUP BY c.paciente.id, c.medico.id, c.medico.especificacaoMedica.id, c.paciente.genero, c.statusConsulta.id
""")
    fun countConcluidos(): List<Array<Any>>


    @Query("SELECT COUNT(c) FROM Consulta c WHERE c.statusConsulta.nomeStatus = 'Cancelada'")
    fun countCancelada(): Long


}
