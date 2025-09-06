package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dominio.Paciente
import org.springframework.data.jpa.repository.JpaRepository

interface EnderecoRespository: JpaRepository<Endereco, Int> {
    fun findByCep(cep: String): Endereco?
}