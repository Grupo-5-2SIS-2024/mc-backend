package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.PossivelCliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface PossivelClienteRepository : JpaRepository<PossivelCliente, Int> {
    fun findByEmail(email: String): PossivelCliente?

    @Query("""
    SELECT CASE 
               WHEN COUNT(DISTINCT pc.id) = 0 THEN 0.0 
               ELSE (COUNT(DISTINCT p.id) / COUNT(DISTINCT pc.id)) * 100 
           END 
    FROM PossivelCliente pc
    LEFT JOIN Paciente p ON pc.cpf = p.cpf
    WHERE pc.dataNascimento >= CURRENT_DATE - 6 MONTH
""")
    fun percentualLeadsConvertidos(): Double
}