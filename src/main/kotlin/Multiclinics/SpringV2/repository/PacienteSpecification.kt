package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Paciente
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

object PacienteSpecification {
    fun filtrar(
        nome: String?,
        sobrenome: String?,
        email: String?,
        cpf: String?,
        genero: String?,
        ativo: Boolean?
    ): Specification<Paciente> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()
            if (!nome.isNullOrBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.lowercase() + "%"))
            }
            if (!sobrenome.isNullOrBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sobrenome")), "%" + sobrenome.lowercase() + "%"))
            }
            if (!email.isNullOrBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.lowercase() + "%"))
            }
            if (!cpf.isNullOrBlank()) {
                predicates.add(cb.like(cb.lower(root.get("cpf")), "%" + cpf.lowercase() + "%"))
            }
            if (!genero.isNullOrBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("genero")), genero.lowercase()))
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get<Boolean>("ativo"), ativo))
            }
            cb.and(*predicates.toTypedArray())
        }
    }
}

