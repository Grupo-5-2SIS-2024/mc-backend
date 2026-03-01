package Multiclinics.SpringV2.dto

data class ConvenioResponse(
    val id: Int?,
    val nome: String?,
    val descricao: String?,
    val ativo: Boolean,
    val planos: List<PlanoResponse>
)

data class PlanoResponse(
    val id: Int?,
    val nome: String?,
    val descricao: String?,
    val ativo: Boolean
)

