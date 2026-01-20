package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import java.time.LocalTime
@Entity
data class CargaHoraria(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var diaSemana: DiaSemana,

    @Column(nullable = false)
    var horaInicio: LocalTime,

    @Column(nullable = false)
    var horaFim: LocalTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    var medico: Medico
)
