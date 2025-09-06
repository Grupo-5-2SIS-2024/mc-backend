package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate

@MappedSuperclass
abstract class Cliente (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null,

    @NotBlank
    @Column(length = 45)
    open var nome: String? = null,

    @NotBlank
    @Column(length = 45)
    open var sobrenome: String? = null,

    @Email
    @Column(length = 45)
    open var email: String? = null,

    @NotBlank
    @CPF
    @Column(length = 11, columnDefinition = "CHAR(11)")
    open var cpf: String? = null,

    @NotBlank
    @Column(length = 11, columnDefinition = "CHAR(11)")
    open var telefone: String? = null,

    @NotBlank
    @Column(length = 45)
    open var genero: String? = null,

    @NotNull
    @Past
    open var dataNascimento: LocalDate? = null,

    ){
}
