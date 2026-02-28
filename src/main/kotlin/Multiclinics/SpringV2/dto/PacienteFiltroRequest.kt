package Multiclinics.SpringV2.dto

data class PacienteFiltroRequest(
    val page: Int? = null,
    val size: Int? = null,
    val nome: String? = null,
    val sobrenome: String? = null,
    val email: String? = null,
    val cpf: String? = null,
    val genero: String? = null,
    val ativo: Boolean? = null
)

