package Multiclinics.SpringV2.dominio

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Entity
data class StatusConsulta (
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,

    @field:NotBlank(message = "O status não pode estar em branco.")
    var nomeStatus: String?
)
