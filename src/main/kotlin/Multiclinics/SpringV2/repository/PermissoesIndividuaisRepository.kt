package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.PermissoesIndividuais
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PermissoesIndividuaisRepository : JpaRepository<PermissoesIndividuais, Int> {
    fun findByMedicoId(medicoId: Int): Optional<PermissoesIndividuais>
}