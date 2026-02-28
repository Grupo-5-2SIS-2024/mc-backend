package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Medico
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

object MedicoSpecification {
    fun filtrar(
        nome: String?,
        sobrenome: String?,
        email: String?,
        crm: String?,
        especialidade: String?,
        ativo: Boolean?
    ): Specification<Medico> {
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
            if (!crm.isNullOrBlank()) {
                predicates.add(cb.like(cb.lower(root.get("carterinha")), "%" + crm.lowercase() + "%"))
            }
            if (!especialidade.isNullOrBlank()) {
                val join = root.join<Any, Any>("especificacaoMedica")
                predicates.add(cb.like(cb.lower(join.get<String>("area")), "%" + especialidade.lowercase() + "%"))
            }
            if (ativo != null) {
                predicates.add(cb.equal(root.get<Boolean>("ativo"), ativo))
            }
            cb.and(*predicates.toTypedArray())
        }
    }
}
