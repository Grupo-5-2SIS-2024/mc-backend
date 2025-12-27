package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

@Entity
@Table(name = "permissoes_individuais")
data class PermissoesIndividuais(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "medico_id", nullable = false)
    var medicoId: Int,

    @Column(name = "permissoes", columnDefinition = "TEXT", nullable = false)
    var permissoes: String,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "data_atualizacao", nullable = false)
    var dataAtualizacao: LocalDateTime = LocalDateTime.now()
)



