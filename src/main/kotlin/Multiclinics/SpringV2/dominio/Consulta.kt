package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class Consulta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @NotNull(message = "A data e hora da consulta não podem ser nulas.")
    @Column(name = "datahora_consulta")
    var datahoraConsulta: LocalDateTime? = null,

    @NotBlank(message = "A descrição da consulta não pode estar em branco.")
    var descricao: String? = null,

    @ManyToOne
    @JoinColumn(name = "medico")
    var medico: Medico? = null,

    @ManyToOne
    @JoinColumn(name = "especificacao_medica")
    var especificacaoMedica: EspecificacaoMedica? = null,

    @ManyToOne
    @JoinColumn(name = "status_consulta")
    var statusConsulta: StatusConsulta? = null,

    @ManyToOne
    @JoinColumn(name = "paciente")
    var paciente: Paciente? = null,

    @NotNull(message = "A duração da consulta não pode ser nula.")
    @Column(name = "duracao_Consulta")
    var duracaoConsulta: LocalTime? = null
)
