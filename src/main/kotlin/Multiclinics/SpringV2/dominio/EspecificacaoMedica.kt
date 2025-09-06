package Multiclinics.SpringV2.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Entity
 data class EspecificacaoMedica (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    @field:NotBlank(message = "A area não pode estar em branco.")
    var area: String?
 )