package Multiclinics.SpringV2.dto

import jakarta.validation.constraints.NotNull

data class ConsultaRecorrenteRequest(
    @field:NotNull val dataInicial: String,        // yyyy-MM-dd
    @field:NotNull val hora: String,               // HH:mm
    @field:NotNull val semanas: Int,               // ex 30
    @field:NotNull val medicoId: Int,
    @field:NotNull val pacienteId: Int,
    @field:NotNull val especificacaoMedicaId: Int,
    @field:NotNull val statusConsultaId: Int = 1,
    @field:NotNull val duracaoMin: Int,            // 30, 50, 60
    val descricao: String? = null
)

data class ConsultaRecorrenteItemResult(
    val data: String,
    val hora: String,
    val status: String,            // CREATED, SKIPPED_CONFLICT, ERROR
    val consultaId: Int? = null,
    val motivo: String? = null
)

data class ConsultaRecorrenteResponse(
    val created: Int,
    val skipped: Int,
    val errors: Int,
    val itens: List<ConsultaRecorrenteItemResult>
)