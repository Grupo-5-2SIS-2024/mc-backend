package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.SalaAtendimento
import org.springframework.data.jpa.repository.JpaRepository

interface SalaAtendimentoRepository : JpaRepository<SalaAtendimento, Int> {
    fun findAllByOrderByNomeAsc(): List<SalaAtendimento>
}