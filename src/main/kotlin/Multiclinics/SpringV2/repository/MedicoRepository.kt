package Multiclinics.SpringV2.repository

import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dto.GraficoGeralAdm
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query


interface MedicoRepository: JpaRepository<Medico, Int>, JpaSpecificationExecutor<Medico> {

   fun findByEmail(email: String): Medico?

   fun findByEmailAndSenha(email: String, senha: String): Medico?
   
   // Busca todos os médicos ordenados alfabeticamente por nome e sobrenome
   fun findAllByOrderByNomeAscSobrenomeAsc(): List<Medico>

   @Query("SELECT COUNT(*) FROM Medico m WHERE m.permissao.id = (SELECT p.id FROM Permissionamento p WHERE p.nome = 'Admin')")
   fun totalAdministradores(): Long

   @Query("SELECT COUNT(*) FROM Medico m WHERE m.ativo = TRUE AND m.permissao.id = (SELECT p.id FROM Permissionamento p WHERE p.nome = 'Admin')")
   fun totalAdministradoresAtivos(): Long

   @Query(
      "SELECT new Multiclinics.SpringV2.dto.GraficoGeralAdm(em.area, COUNT(DISTINCT p.id), COUNT(DISTINCT m.id), COUNT(DISTINCT c.id)) " +
              "FROM EspecificacaoMedica em " +
              "LEFT JOIN Medico m ON m.especificacaoMedica = em " + // Ajuste aqui
              "LEFT JOIN m.consultas c " +
              "LEFT JOIN c.paciente p " +
              "GROUP BY em.area"
      )
   fun obterDadosGraficoGeral(): List<GraficoGeralAdm>

   fun findAllByAtivoTrue(): List<Medico>
   
   // Busca médicos ativos ordenados alfabeticamente
   fun findAllByAtivoTrueOrderByNomeAscSobrenomeAsc(): List<Medico>
}
