package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Responsavel
import Multiclinics.SpringV2.repository.ResponsavelRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.*

class ResponsavelServiceTest{
    private lateinit var responsavelRepository: ResponsavelRepository
    private lateinit var responsavelService: ResponsavelService

    @BeforeEach
    fun configurar() {
        responsavelRepository = mock(ResponsavelRepository::class.java)
        responsavelService = ResponsavelService(responsavelRepository)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val listaVazia = emptyList<Responsavel>()
        assertThrows(ResponseStatusException::class.java) {
            responsavelService.validarLista(listaVazia)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val listaNaoVazia = listOf(Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        assertDoesNotThrow {
            responsavelService.validarLista(listaNaoVazia)
        }
    }

    @Test
    fun `salvar deve lançar exceção quando o responsavel já existir`() {
        val novoResponsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")
        `when`(responsavelRepository.findByEmail(anyString())).thenReturn(novoResponsavel)

        assertThrows(ResponseStatusException::class.java) {
            responsavelService.salvar(novoResponsavel)
        }
    }

    @Test
    fun `salvar deve retornar responsavel salvo quando ele não existir`() {
        val novoResponsavel = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")
        `when`(responsavelRepository.findByEmail(anyString())).thenReturn(null)
        `when`(responsavelRepository.save(any(Responsavel::class.java))).thenReturn(novoResponsavel)

        val resposta = responsavelService.salvar(novoResponsavel)
        assertEquals(novoResponsavel, resposta)
    }

    @Test
    fun `atualizar deve retornar 404 quando o responsavel não existir`() {
        `when`(responsavelRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = responsavelService.atualizar(1, Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 quando o responsavel existir`() {
        val responsavelExistente = Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino")
        `when`(responsavelRepository.findById(anyInt())).thenReturn(Optional.of(responsavelExistente))
        `when`(responsavelRepository.save(any(Responsavel::class.java))).thenReturn(responsavelExistente)

        val resposta = responsavelService.atualizar(1, Responsavel(id = 1, nome = "Atualizado", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals("Atualizado", resposta.body?.nome)
    }

    @Test
    fun `deletar deve lançar exceção quando o responsavel não existir`() {
        `when`(responsavelRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            responsavelService.deletar(1)
        }
    }

    @Test
    fun `deletar deve chamar deleteById quando o responsavel existir`() {
        `when`(responsavelRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            responsavelService.deletar(1)
        }
        verify(responsavelRepository, times(1)).deleteById(1)
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"), Responsavel(id = 1, nome = "João", sobrenome = "Silva", email = "joao@example.com", telefone = "123456789", cpf = "123.456.789-00", endereco = null, Genero = "Masculino"))
        `when`(responsavelRepository.findAll()).thenReturn(lista)

        val resultado = responsavelService.getLista()
        assertEquals(2, resultado.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(responsavelRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            responsavelService.getLista()
        }
    }
}
