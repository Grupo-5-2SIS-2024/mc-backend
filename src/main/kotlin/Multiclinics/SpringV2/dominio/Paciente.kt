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

   @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
   @JoinColumn(name = "responsavel", nullable = true)
   var responsavel: Responsavel? = null,

   @ManyToOne
   var endereco: Endereco? = null
):Cliente(){

}
