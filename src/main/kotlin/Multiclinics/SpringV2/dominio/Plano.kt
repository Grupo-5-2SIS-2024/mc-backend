package Multiclinics.SpringV2.dominio

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "plano")
class Plano(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @NotBlank
    @Column(length = 100)
    var nome: String? = null,

    @Column(length = 500)
    var descricao: String? = null,

    @Column(nullable = false)
    var ativo: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "convenio_id", nullable = false)
    @JsonBackReference
    var convenio: Convenio? = null
)

