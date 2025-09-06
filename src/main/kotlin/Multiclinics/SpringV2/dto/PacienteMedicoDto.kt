package Multiclinics.SpringV2.dto

import Multiclinics.SpringV2.dominio.Paciente


class PacienteMedicoDto(

    var paciente: Paciente? = null,
    var nomeMedico: String? = null,
    var especialidadeMedica: String? = null,

) {
}