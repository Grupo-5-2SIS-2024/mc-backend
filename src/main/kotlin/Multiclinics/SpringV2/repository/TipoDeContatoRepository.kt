package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.dominio.TipoDeContato
import org.springframework.data.jpa.repository.JpaRepository

interface TipoDeContatoRepository: JpaRepository<TipoDeContato, Int> {
    fun findByFaseContato(FaseContato: String): TipoDeContato?
}