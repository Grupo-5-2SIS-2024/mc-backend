package Multiclinics.SpringV2.dto

data class MedicoFiltroRequest(
    val page: Int? = null,
    val size: Int? = null,
    val nome: String? = null,
    val sobrenome: String? = null,
    val email: String? = null,
    val crm: String? = null,
    val especialidade: String? = null,
    val ativo: Boolean? = null
)

