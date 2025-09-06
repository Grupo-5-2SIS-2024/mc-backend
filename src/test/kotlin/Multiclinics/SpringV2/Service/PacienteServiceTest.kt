package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.Integration.ViaCepClient
import Multiclinics.SpringV2.Integration.dto.EnderecoCepDto
import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.dominio.Paciente
import Multiclinics.SpringV2.dominio.Responsavel
import Multiclinics.SpringV2.dto.PacienteCriacaoDto
import Multiclinics.SpringV2.repository.PacienteRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.*

class PacienteServiceTest{

    private lateinit var pacienteRepository: PacienteRepository
    private lateinit var enderecoService: EnderecoService
    private lateinit var responsavelService: ResponsavelService
    private lateinit var viacepClient: ViaCepClient
    private lateinit var pacienteService: PacienteService
    private lateinit var modelMapper: ModelMapper

    @BeforeEach
    fun configurar() {
        pacienteRepository = mock(PacienteRepository::class.java)
        enderecoService = mock(EnderecoService::class.java)
        responsavelService = mock(ResponsavelService::class.java)
        viacepClient = mock(ViaCepClient::class.java)
        modelMapper = ModelMapper()
        pacienteService = PacienteService(pacienteRepository, enderecoService, responsavelService, modelMapper, viacepClient)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val listaVazia = emptyList<Paciente>()
        assertThrows(ResponseStatusException::class.java) {
            pacienteService.validarLista(listaVazia)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val listaNaoVazia = listOf(Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")))
        assertDoesNotThrow {
            pacienteService.validarLista(listaNaoVazia)
        }
    }

    @Test
    fun `salvar deve lançar exceção quando o email do paciente já existe`() {
        val novoPacienteDto = PacienteCriacaoDto( cpf= "37637602885", nome= "gabriel", sobrenome="Sanchez", genero= "masculino", telefone= "1197501231" ,email = "paciente@teste.com", cep = "12345-678", dataNascimento= LocalDate.now().minusYears(20))
        `when`(pacienteRepository.existsByEmail(anyString())).thenReturn(true)

        assertThrows(ResponseStatusException::class.java) {
            pacienteService.salvar(novoPacienteDto)
        }
    }

//    @Test
//    fun `salvar deve criar paciente quando o email não existe`() {
//        val novoPacienteDto = PacienteCriacaoDto( cpf= "37637602885", nome= "gabriel", sobrenome="Sanchez", genero= "masculino", telefone= "1197501231" ,email = "paciente@teste.com", cep = "12345-678", dataNascimento= LocalDate.now().minusYears(20))
//        val enderecoResponse = ResponseEntity(Endereco(cep = "12345-678", logradouro = "jurupis", bairro = "moema", complemento = "string"), HttpStatus.OK)
//        `when`(pacienteRepository.existsByEmail(anyString())).thenReturn(false)
//        `when`(viacepClient.cep(anyString())).thenReturn(enderecoResponse)
//        `when`(enderecoService.criar(any(Endereco::class.java))).thenReturn(Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"))
//        `when`(pacienteRepository.save(any(Paciente::class.java))).thenReturn(Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")))
//
//        assertDoesNotThrow {
//            pacienteService.salvar(novoPacienteDto)
//        }
//    }

    @Test
    fun `atualizar deve retornar 404 quando o paciente não existe`() {
        val novoPaciente = Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        `when`(pacienteRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = pacienteService.atualizar(1, novoPaciente)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 e atualizar o paciente quando ele existe`() {
        val pacienteExistente = Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        val novoPaciente = Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        `when`(pacienteRepository.findById(anyInt())).thenReturn(Optional.of(pacienteExistente))
        `when`(pacienteRepository.save(any(Paciente::class.java))).thenReturn(novoPaciente)

        val resposta = pacienteService.atualizar(1, novoPaciente)
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals(novoPaciente.nome, resposta.body?.nome)
    }

    @Test
    fun `deletar deve lançar exceção quando o paciente não existe`() {
        `when`(pacienteRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            pacienteService.deletar(1)
        }
    }

    @Test
    fun `deletar deve deletar o paciente quando ele existe`() {
        `when`(pacienteRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            pacienteService.deletar(1)
        }
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")), Paciente(id=1,nome = "Novo Nome", sobrenome = "Sanchez", cpf= "37637602885",genero="Masculino", email="gabriel@gmail.com",cns="123243525", telefone = "11975012301", dtEntrada = LocalDate.now().minusYears(5), dtSaida = LocalDate.now().minusYears(3), dtNasc =  LocalDate.now().minusYears(10), endereco = Endereco(id = 1, cep = "37637602885", logradouro =  "Rua Haddock Lobo", complemento =  "até 1079 - lado ímpar", bairro = "Cerqueira César"), responsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")))
        `when`(pacienteRepository.findAll()).thenReturn(lista)

        val resultado = pacienteService.getLista()
        assertEquals(2, resultado.size)
    }

//    @Test
//    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
//        `when`(pacienteRepository.findAll()).thenReturn(emptyList())
//
//        assertThrows(ResponseStatusException::class.java) {
//            pacienteService.getLista()
//        }
//    }

//    @Test
//    fun `getConversoesUltimosSeisMeses deve retornar dados corretos`() {
//        val dados = listOf(
//            arrayOf("Janeiro", 10),
//            arrayOf("Fevereiro", 15)
//        )
//        `when`(pacienteRepository.countPossiveisClientesConvertidos()).thenReturn(dados)
//
//        val resultado = pacienteService.getConversoesUltimosSeisMeses()
//        assertEquals(2, resultado.size)
//        assertEquals("Janeiro", resultado[0]["mes"])
//        assertEquals(10, resultado[0]["total"])
//        assertEquals("Fevereiro", resultado[1]["mes"])
//        assertEquals(15, resultado[1]["total"])
//    }
}