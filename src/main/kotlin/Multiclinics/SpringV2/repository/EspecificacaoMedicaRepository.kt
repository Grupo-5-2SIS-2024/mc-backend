package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.EspecificacaoMedica
import org.springframework.data.jpa.repository.JpaRepository

interface EspecificacaoMedicaRepository: JpaRepository<EspecificacaoMedica, Int> {
    fun findByArea(area: String): EspecificacaoMedica?
}