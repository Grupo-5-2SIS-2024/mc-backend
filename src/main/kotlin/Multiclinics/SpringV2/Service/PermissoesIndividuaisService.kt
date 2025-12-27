package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.PermissoesIndividuais
import Multiclinics.SpringV2.repository.PermissoesIndividuaisRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PermissoesIndividuaisService(
    private val repository: PermissoesIndividuaisRepository
) {

    fun salvarPermissoes(medicoId: Int, permissoes: String): PermissoesIndividuais {
        val permissoesExistente = repository.findByMedicoId(medicoId)
        
        return if (permissoesExistente.isPresent) {
            val permissao = permissoesExistente.get()
            permissao.permissoes = permissoes
            permissao.dataAtualizacao = LocalDateTime.now()
            repository.save(permissao)
        } else {
            val novaPermissao = PermissoesIndividuais(
                medicoId = medicoId,
                permissoes = permissoes,
                dataAtualizacao = LocalDateTime.now()
            )
            repository.save(novaPermissao)
        }
    }

    fun buscarPermissoes(medicoId: Int): String? {
        val permissoes = repository.findByMedicoId(medicoId)
        return if (permissoes.isPresent) {
            permissoes.get().permissoes
        } else {
            null
        }
    }

    fun deletarPermissoes(medicoId: Int) {
        val permissoes = repository.findByMedicoId(medicoId)
        if (permissoes.isPresent) {
            repository.delete(permissoes.get())
        }
    }
}