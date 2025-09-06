package Multiclinics.SpringV2.dto

import Multiclinics.SpringV2.dominio.Endereco
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

class PacienteSemResponsavel(
    @field: Size(min = 3)
    var nome: String?,

    @field: Size(min = 3)
    var sobrenome: String?,

    @field:Email(message = "O email fornecido não é válido.")
    var email: String?,

    @field:NotBlank(message = "O CPF não pode estar em branco.")
    @field:CPF(message = "O CPF fornecido não é válido.")
    var cpf: String?,

    @field:NotBlank(message = "O genero não pode estar em branco.")
    var genero: String?,

    @field:NotBlank(message = "O telefone não pode estar em branco.")
    var telefone: String?,

    @field:NotNull
    @field:Past
    var dataNascimento: LocalDate?,

    @Column(length = 15)
    var cns: String?,

    @field:Column(length = 10 * 1024 * 1024)
    var foto:String? = null,

    open var endereco: Endereco? = null
)