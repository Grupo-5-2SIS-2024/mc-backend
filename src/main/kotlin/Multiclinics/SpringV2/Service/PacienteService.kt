package Multiclinics.SpringV2.Service


import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.Paciente
import Multiclinics.SpringV2.dto.PacienteComResponsavel
import Multiclinics.SpringV2.dto.PacienteMedicoDto
import Multiclinics.SpringV2.dto.PacienteSemResponsavel
import Multiclinics.SpringV2.repository.EnderecoRespository
import Multiclinics.SpringV2.repository.PacienteRepository
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Service
class PacienteService(
    val pacienteRepository: PacienteRepository,
    val enderecoService: EnderecoService,
    val responsavelService: ResponsavelService,
    val modelMapper: ModelMapper = ModelMapper(),

) {
    //@PersistenceContext
    //private lateinit var entityManager: EntityManager
    fun validarLista(lista: List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun salvar(novoPaciente: PacienteComResponsavel): Paciente {

        // mapeando dto para dominio para poder cadastrar no banco (a dominio não tem o cep)
        val pacienteDominio = modelMapper.map(novoPaciente, Paciente::class.java)
      //  pacienteDominio.responsavel = pacienteDominio.responsavel?.let {entityManager.merge(it) }
        // verifica se o email existe, caso existe, retorna conflito

        if (pacienteRepository.existsByEmail(pacienteDominio.email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT)
        }


        var endereco = novoPaciente.endereco!!
        novoPaciente.endereco = enderecoService.criar(endereco)
        pacienteDominio.endereco = novoPaciente.endereco
        // salve o paciente já com o endereço vinculado
        var oi =  pacienteRepository.save(pacienteDominio)
         return oi

    }

    fun salvarSemResponsavel(novoPaciente: PacienteSemResponsavel): Paciente {

        // mapeando dto para dominio para poder cadastrar no banco (a dominio não tem o cep)
        val pacienteDominio = modelMapper.map(novoPaciente, Paciente::class.java)
        pacienteDominio.responsavel = null

        // verifica se o email existe, caso existe, retorna conflito
        if (pacienteRepository.existsByEmail(pacienteDominio.email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT)
        }

        // salve o paciente já com o endereço vinculado

        var endereco = novoPaciente.endereco!!
        novoPaciente.endereco = enderecoService.criar(endereco)
        pacienteDominio.endereco = novoPaciente.endereco

        var oi = pacienteRepository.save(pacienteDominio)

        return oi

    }


    fun atualizar(id: Int, novoPaciente: Paciente): ResponseEntity<Paciente> {
        val pacienteExistente = pacienteRepository.findById(id)
        return if (pacienteExistente.isPresent) {
            val pacienteEscolhido = pacienteExistente.get()
            pacienteEscolhido.apply {
                nome = novoPaciente.nome
                sobrenome = novoPaciente.sobrenome
                email = novoPaciente.email
                cpf = novoPaciente.cpf
                genero = novoPaciente.genero
                telefone = novoPaciente.telefone
                dataNascimento = novoPaciente.dataNascimento
                foto = novoPaciente.foto

            }

            val pacienteAtualizado = pacienteRepository.save(pacienteEscolhido)
            ResponseEntity.status(200).body(pacienteAtualizado)
        } else {
            ResponseEntity.status(404).build()
        }
    }


    fun deletar(id: Int) {
        if (!pacienteRepository.existsById(id)) {
            throw IllegalArgumentException("Paciente não encontrado")
        }

        pacienteRepository.deleteById(id)

    }

    fun getLista(): List<Paciente> {
        val lista = pacienteRepository.findAll()
        validarLista(lista)
        return lista
    }

    fun buscarPacientePorId(id: Int): ResponseEntity<Paciente> {
        val pacienteExistente = pacienteRepository.findById(id)
        return if (pacienteExistente.isPresent) {
            ResponseEntity.ok(pacienteExistente.get())
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    fun getConversoesUltimosSeisMeses(): List<Map<String, Any>> {
        val result = pacienteRepository.countPossiveisClientesConvertidos()

        // Agrupando os registros por data de conversão
        val groupedResult = result.groupBy { it[0] } // Agrupa por dataConversao

        return groupedResult.map { (dataConversao, records) ->
            mapOf(
                "dataConversao" to dataConversao,
                "totalConvertidos" to records.sumOf { it[1] as Long }, // Soma totalConvertidos por data
                "details" to records.map {
                    mapOf(
                        "pacienteId" to (it.getOrNull(2)?.toString() ?: "null"),
                        "medicoId" to (it.getOrNull(3)?.toString() ?: "null"),
                        "especMedicaId" to (it.getOrNull(4)?.toString() ?: "null"),
                        "genero" to (it.getOrNull(5)?.toString() ?: "null"),
                        "statusConsultaId" to (it.getOrNull(6)?.toString() ?: "null")
                    )
                }
            )
        }
    }


    fun calcularPorcentagemPacientesABA(): Double {
        return pacienteRepository.calcularPorcentagemPacientesABA()
    }

    fun contarPacientesAtivos(): Long {
        return pacienteRepository.contarPacientesAtivos()
    }

    fun contarPacientesUltimoTrimestre(): Long {
        return pacienteRepository.contarPacientesUltimoTrimestre()
    }

    fun contarAgendamentosVencidos(): Long {
        return pacienteRepository.contarAgendamentosVencidos()
    }
}