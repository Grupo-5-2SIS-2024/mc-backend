package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "sala_atendimento")
data class SalaAtendimento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @field:NotBlank
    @Column(nullable = false, unique = true, length = 100)
    var nome: String? = null
)