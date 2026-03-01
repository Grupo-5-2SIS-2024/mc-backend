package Multiclinics.SpringV2.dominio

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "convenio")
class Convenio(
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

    @OneToMany(mappedBy = "convenio", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var planos: MutableList<Plano> = mutableListOf()
)

