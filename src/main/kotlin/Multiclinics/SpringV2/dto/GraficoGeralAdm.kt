package Multiclinics.SpringV2.dto

data class GraficoGeralAdm(
    val especializacao: String,
    val pacientes: Long,
    val medicos: Long,
    val consultas: Long
)