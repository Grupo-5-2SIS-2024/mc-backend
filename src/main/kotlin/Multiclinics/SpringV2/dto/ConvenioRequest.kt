package Multiclinics.SpringV2.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConvenioRequest(
    val nome: String,
    val descricao: String?,
    val ativo: Boolean = true,
    val planos: List<PlanoRequest>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlanoRequest(
    val nome: String,
    val descricao: String?,
    val ativo: Boolean = true
)

