package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dto.GraficoGeralAdm
import Multiclinics.SpringV2.repository.MedicoRepository
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class MedicoService(
    val medicoRepository: MedicoRepository,
    val modelMapper: ModelMapper = ModelMapper(),
) {
    fun validarLista(lista:List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun salvar(novoMedico: Medico) {

        val medicoExistente = medicoRepository.findByEmail(novoMedico.email?:"")
        if (medicoExistente != null) {
            throw ResponseStatusException(
                HttpStatusCode.valueOf(404))
        } else {
            medicoRepository.save(novoMedico)
            ResponseEntity.status(201).body(novoMedico)
        }
    }
    fun atualizar(id: Int ,novoMedico: Medico): ResponseEntity<Medico>{
        val medicoExistente = medicoRepository.findById(id)
        if (medicoExistente.isPresent) {
            var medicoEscolhido = medicoExistente.get()

            medicoEscolhido.nome = novoMedico.nome
            medicoEscolhido.sobrenome = novoMedico.sobrenome
            medicoEscolhido.carterinha = novoMedico.carterinha
            medicoEscolhido.telefone = novoMedico.telefone
            medicoEscolhido.dataNascimento = novoMedico.dataNascimento
            medicoEscolhido.email = novoMedico.email
            medicoEscolhido.senha = novoMedico.senha
            medicoEscolhido.cpf = novoMedico.cpf
            medicoEscolhido.especificacaoMedica = novoMedico.especificacaoMedica
            medicoEscolhido.permissao = novoMedico.permissao
            medicoEscolhido.foto = novoMedico.foto

            val medicoAtualizado = medicoRepository.save(medicoEscolhido)
            return ResponseEntity.status(200).body(medicoAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }

    }
    fun deletar(id: Int) {
        if (!medicoRepository.existsById(id)) {
            throw IllegalArgumentException("Médico não encontrado")
        }

        medicoRepository.deleteById(id)

    }


    fun getLista():List<Medico> {
        val lista = medicoRepository.findAll()
        validarLista(lista)

        return lista
    }

    fun listarPorId(id: Int): Medico {
        return medicoRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatusCode.valueOf(404), "Médico não encontrado")
        }
    }

    fun totalAdministradores(): Long {
        return medicoRepository.totalAdministradores()
    }

    fun totalAdministradoresAtivos(): Long {
        return medicoRepository.totalAdministradoresAtivos()
    }

    fun obterDadosGraficoGeral(): List<GraficoGeralAdm> {
        return medicoRepository.obterDadosGraficoGeral()
    }

}