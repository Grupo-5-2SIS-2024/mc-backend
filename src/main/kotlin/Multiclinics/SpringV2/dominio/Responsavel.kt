package Multiclinics.SpringV2.dominio

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate
import java.time.Period
import java.util.*

@Entity
class Responsavel:Cliente(){
    @JsonIgnore
    @ManyToMany(mappedBy = "responsaveis")
    var pacientes: MutableList<Paciente> = mutableListOf()


}
