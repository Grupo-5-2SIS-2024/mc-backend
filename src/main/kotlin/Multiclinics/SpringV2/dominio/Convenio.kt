package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class Convenio(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @NotBlank
    @Column(length = 100, unique = true)
    var nome: String? = null,

    @Column(length = 500)
    var descricao: String? = null,

    @Column(nullable = false)
    var ativo: Boolean = true,

    @OneToMany(mappedBy = "convenio", cascade = [CascadeType.ALL], orphanRemoval = true)
    var planos: MutableList<Plano> = mutableListOf()
)

