package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "possivel_cliente")
data class PossivelCliente(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @field:NotBlank(message = "O nome não pode estar em branco.")
    @Column(length = 45)
    var nome: String?,

    @field:NotBlank(message = "O sobrenome não pode estar em branco.")
    @Column(length = 45)
    var sobrenome: String?,

    @field:Email(message = "O email fornecido não é válido.")
    @Column(length = 45)
    var email: String?,

    @field:NotBlank(message = "O CPF não pode estar em branco.")
    @field:Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "O CPF fornecido não é válido.")
    @Column(length = 14, unique = true)
    var cpf: String?,

    @field:NotBlank(message = "O telefone não pode estar em branco.")
    @Column(length = 15)
    var telefone: String?,

    @field:NotNull(message = "A data de nascimento não pode estar em branco.")
    @Column(name = "dt_nasc")
    var dataNascimento: LocalDate?,

    @field:NotBlank(message = "A fase do lead não pode estar em branco.")
    @Column(length = 45)
    var fase: String?,

    @Column(name = "data_insercao", nullable = false, updatable = false)
    var dataInsercao: LocalDateTime? = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "tipo_de_contato")
    var tipoDeContato: TipoDeContato?
)
