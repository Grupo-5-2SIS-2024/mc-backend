package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Notas

import org.springframework.data.jpa.repository.JpaRepository

interface NotasRepository: JpaRepository<Notas, Int> {

}