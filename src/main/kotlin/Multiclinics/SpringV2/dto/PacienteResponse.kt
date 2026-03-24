package Multiclinics.SpringV2.dto

import Multiclinics.SpringV2.dominio.Endereco
import java.time.LocalDate

data class PacienteResponse (
    val id: Int?,
    val nome: String?,
    val sobrenome: String?,
    val email: String?,
    val telefone: String?,
    val cpf: String?,
    val genero: String?,
    val dataNascimento: LocalDate?,
    val cns: String?,
    val foto: String?,
    val endereco: Endereco?,
    val plano: PlanoPacienteResponse?
)

data class PlanoPacienteResponse(
    val id: Int?,
    val nome: String?,
    val convenioId: Int?,
    val convenioNome: String?
)