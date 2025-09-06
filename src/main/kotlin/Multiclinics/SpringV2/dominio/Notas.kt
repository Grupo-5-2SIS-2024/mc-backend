package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class Notas(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "titulo")
    @NotBlank(message = "O título não pode estar em branco.")
    var titulo: String? = null,

    @Column(name = "descricao")
    @NotBlank(message = "A descrição não pode estar em branco.")
    var descricao: String? = null,

    @ManyToOne
    @JoinColumn(name = "medico")
    var medico: Medico? = null
)
