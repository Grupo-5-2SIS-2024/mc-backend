package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.repository.MedicoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class MedicoServiceTest{
    private lateinit var medicoRepository: MedicoRepository
    private lateinit var medicoService: MedicoService

    @BeforeEach
    fun configurar() {
        medicoRepository = mock(MedicoRepository::class.java)
        medicoService = MedicoService(medicoRepository, ModelMapper())
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val listaVazia = emptyList<Medico>()
        assertThrows(ResponseStatusException::class.java) {
            medicoService.validarLista(listaVazia)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val listaNaoVazia = listOf(Medico())
        assertDoesNotThrow {
            medicoService.validarLista(listaNaoVazia)
        }
    }

    @Test
    fun `salvar deve lançar exceção quando o medico já existe`() {
        val novoMedico = Medico(email = "medico@teste.com")
        `when`(medicoRepository.findByEmail(anyString())).thenReturn(novoMedico)

        assertThrows(ResponseStatusException::class.java) {
            medicoService.salvar(novoMedico)
        }
    }

    @Test
    fun `salvar deve salvar o medico quando ele não existe`() {
        val novoMedico = Medico(email = "medico@teste.com")
        `when`(medicoRepository.findByEmail(anyString())).thenReturn(null)
        `when`(medicoRepository.save(any(Medico::class.java))).thenReturn(novoMedico)

        assertDoesNotThrow {
            medicoService.salvar(novoMedico)
        }
    }

    @Test
    fun `atualizar deve retornar 404 quando o medico não existe`() {
        val novoMedico = Medico(nome = "Novo Nome")
        `when`(medicoRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = medicoService.atualizar(1, novoMedico)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 e atualizar o medico quando ele existe`() {
        val medicoExistente = Medico(id = 1, nome = "Nome Antigo")
        val novoMedico = Medico(id = 1, nome = "Novo Nome")
        `when`(medicoRepository.findById(anyInt())).thenReturn(Optional.of(medicoExistente))
        `when`(medicoRepository.save(any(Medico::class.java))).thenReturn(novoMedico)

        val resposta = medicoService.atualizar(1, novoMedico)
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals(novoMedico.nome, resposta.body?.nome)
    }

    @Test
    fun `deletar deve lançar exceção quando o medico não existe`() {
        `when`(medicoRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            medicoService.deletar(1)
        }
    }

    @Test
    fun `deletar deve deletar o medico quando ele existe`() {
        `when`(medicoRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            medicoService.deletar(1)
        }
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Medico(id = 1, nome = "Medico 1"), Medico(id = 2, nome = "Medico 2"))
        `when`(medicoRepository.findAll()).thenReturn(lista)

        val resultado = medicoService.getLista()
        assertEquals(2, resultado.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(medicoRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            medicoService.getLista()
        }
    }

    @Test
    fun `listarPorId deve lançar exceção quando o medico não existe`() {
        `when`(medicoRepository.findById(anyInt())).thenReturn(Optional.empty())

        assertThrows(ResponseStatusException::class.java) {
            medicoService.listarPorId(1)
        }
    }

    @Test
    fun `listarPorId deve retornar o medico quando ele existe`() {
        val medico = Medico(id = 1, nome = "Medico 1")
        `when`(medicoRepository.findById(anyInt())).thenReturn(Optional.of(medico))

        val resultado = medicoService.listarPorId(1)
        assertEquals(medico, resultado)
    }
}