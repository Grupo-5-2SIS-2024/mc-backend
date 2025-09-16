package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.br.CPF

@Entity
data class Endereco(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    var cep: String?,
    var logradouro: String?,
    var complemento: String?,
    var bairro: String?,
    var numero: String?
    )
