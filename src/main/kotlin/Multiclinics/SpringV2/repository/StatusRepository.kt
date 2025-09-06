package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.dominio.StatusConsulta
import org.springframework.data.jpa.repository.JpaRepository

interface StatusRepository: JpaRepository<StatusConsulta, Int> {
    fun findByNomeStatus(nomeStatus: String): StatusConsulta?
}