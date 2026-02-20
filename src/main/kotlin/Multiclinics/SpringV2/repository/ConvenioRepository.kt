package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Convenio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ConvenioRepository : JpaRepository<Convenio, Int> {
    
    fun findByNome(nome: String): Convenio?
    
    fun existsByNome(nome: String): Boolean
    
    fun findAllByOrderByNomeAsc(): List<Convenio>
    
    fun findByAtivoTrue(): List<Convenio>
    
    @Query("SELECT c FROM Convenio c WHERE c.ativo = true ORDER BY c.nome ASC")
    fun findAllAtivosOrdenados(): List<Convenio>
}

