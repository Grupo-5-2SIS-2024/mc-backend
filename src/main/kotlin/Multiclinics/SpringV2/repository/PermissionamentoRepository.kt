package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dominio.Permissionamento
import org.springframework.data.jpa.repository.JpaRepository

interface PermissionamentoRepository: JpaRepository<Permissionamento, Int> {
    fun findByNome(Nome: String): Permissionamento?
}