package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class Acompanhamento(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    @field:NotBlank(message = "O resumo não pode estar em branco.")
    var resumo: String?,

   @field:NotBlank(message = "O Relatorio não pode estar em branco.")
    var Relatorio: String?,

    /*@field:Column(length = 3 * 1024 * 1024)
    var Relatorio:ByteArray,*/


    @field: ManyToOne
    var consulta: Consulta
)
