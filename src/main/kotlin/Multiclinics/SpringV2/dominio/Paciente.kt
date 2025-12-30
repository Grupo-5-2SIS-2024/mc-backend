package Multiclinics.SpringV2.dominio

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate
import java.time.Period

@Entity
data class Paciente(

   var dtEntrada: LocalDate?,

   var dtSaida: LocalDate?,

   @Column(length = 15)
   var cns: String?,

   @field:Column(length = 10 * 1024 * 1024)
   var foto:String? = null,

   @ManyToMany
   @JoinTable(
      name = "paciente_responsavel",
      joinColumns = [JoinColumn(name = "paciente_id")],
      inverseJoinColumns = [JoinColumn(name = "responsavel_id")]
   )
   var responsaveis: MutableList<Responsavel> = mutableListOf(),


   @ManyToOne
   var endereco: Endereco? = null,

   @Column(nullable = false)
   var ativo: Boolean = true
):Cliente(){

}
