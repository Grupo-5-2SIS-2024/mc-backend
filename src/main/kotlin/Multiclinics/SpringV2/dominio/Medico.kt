package Multiclinics.SpringV2.dominio

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

@Entity
class Medico(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Size(min = 3, max = 45, message = "O nome deve ter entre 3 e 45 caracteres.")
    @Column(length = 45)
    var nome: String? = null,

    @Size(min = 3, max = 45, message = "O sobrenome deve ter entre 3 e 45 caracteres.")
    @Column(length = 45)
    var sobrenome: String? = null,

    @Email(message = "O email fornecido não é válido.")
    @Column(length = 45)
    var email: String? = null,

    @NotBlank(message = "O telefone não pode estar em branco.")
    @Column(length = 45)
    var telefone: String? = null,

    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, max = 45, message = "A senha deve ter entre 6 e 45 caracteres.")
    @Column(length = 45)
    var senha: String? = null,

    @NotBlank(message = "A Carterinha não pode estar em branco.")
    @Column(name = "carteira_representante", length = 45)
    var carterinha: String? = null,

    @ManyToOne
    @JoinColumn(name = "especificacao_medica")
    var especificacaoMedica: EspecificacaoMedica? = null,

    @NotNull(message = "A data de nascimento não pode estar em branco.")
    @Column(name = "dt_nasc")
    var dataNascimento: LocalDate? = null,

    @NotBlank(message = "O CPF não pode estar em branco.")
    @CPF(message = "O CPF fornecido não é válido.")
    @Column(length = 11, columnDefinition = "CHAR(11)")
    var cpf: String? = null,

    @NotNull
    var ativo: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "permissionamento")
    var permissao: Permissionamento? = null,

    @field:Column(length = 10 * 1024 * 1024)
    var foto:String? = null,

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    @JsonIgnore
    val consultas: List<Consulta> = emptyList()
)
