package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Plano
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlanoRepository : JpaRepository<Plano, Int> {
    
    fun findByConvenioId(convenioId: Int): List<Plano>
    
    fun findByConvenioIdAndAtivoTrue(convenioId: Int): List<Plano>
    
    fun findByAtivoTrue(): List<Plano>
    
    @Query("SELECT p FROM Plano p WHERE p.convenio.id = :convenioId AND p.ativo = true ORDER BY p.nome ASC")
    fun findPlanosAtivosByConvenioId(convenioId: Int): List<Plano>
}

