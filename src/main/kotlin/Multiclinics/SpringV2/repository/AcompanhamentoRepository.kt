package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Acompanhamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AcompanhamentoRepository: JpaRepository<Acompanhamento, Int> {
    /*
    @Query("select a.foto from Acompanhamento a where a.codigo = ?1")
    fun recuperarDoc(codigo: Int): ByteArray  */


}