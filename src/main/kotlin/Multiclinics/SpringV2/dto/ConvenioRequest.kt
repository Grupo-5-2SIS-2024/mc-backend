package Multiclinics.SpringV2.dto

data class ConvenioRequest(
    val nome: String,
    val descricao: String?,
    val ativo: Boolean = true,
    val planos: List<PlanoRequest>? = null
)

data class PlanoRequest(
    val nome: String,
    val descricao: String?,
    val ativo: Boolean = true
)

