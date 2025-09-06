package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Acompanhamento
import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.repository.AcompanhamentoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class AcompanhamentoServiceTest{
    private lateinit var acompanhamentoRepository: AcompanhamentoRepository
    private lateinit var acompanhamentoService: AcompanhamentoService

    @BeforeEach
    fun configurar() {
        acompanhamentoRepository = mock(AcompanhamentoRepository::class.java)
        acompanhamentoService = AcompanhamentoService(acompanhamentoRepository)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val listaVazia = emptyList<Acompanhamento>()
        assertThrows<ResponseStatusException> {
            acompanhamentoService.validarLista(listaVazia)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val listaNaoVazia = listOf(Acompanhamento(id = 1, Relatorio = "Relatorio Antigo", resumo = "Resumo Antigo", consulta = Consulta()))
        assertDoesNotThrow {
            acompanhamentoService.validarLista(listaNaoVazia)
        }
    }

    @Test
    fun `atualizar deve retornar 404 quando o acompanhamento não existe`() {
        val novoAcompanhamento = Acompanhamento(id = 1, Relatorio = "Relatorio Antigo", resumo = "Resumo Antigo", consulta = Consulta())
        `when`(acompanhamentoRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = acompanhamentoService.atualizar(1, novoAcompanhamento)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 e atualizar o acompanhamento quando ele existe`() {
        val acompanhamentoExistente = Acompanhamento(id = 1, Relatorio = "Relatorio Antigo", resumo = "Resumo Antigo", consulta = Consulta())
        val novoAcompanhamento = Acompanhamento(id = 1, Relatorio = "Relatorio Novo", resumo = "Resumo Novo", consulta = Consulta())
        `when`(acompanhamentoRepository.findById(anyInt())).thenReturn(Optional.of(acompanhamentoExistente))
        `when`(acompanhamentoRepository.save(any(Acompanhamento::class.java))).thenReturn(novoAcompanhamento)

        val resposta = acompanhamentoService.atualizar(1, novoAcompanhamento)
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals(novoAcompanhamento.Relatorio, resposta.body?.Relatorio)
        assertEquals(novoAcompanhamento.resumo, resposta.body?.resumo)
    }

    @Test
    fun `salvar deve criar novo acompanhamento e retornar 201`() {
        val novoAcompanhamento = Acompanhamento(id = 1, Relatorio = "Relatorio Novo", resumo = "Resumo Novo", consulta = Consulta())
        `when`(acompanhamentoRepository.save(any(Acompanhamento::class.java))).thenReturn(novoAcompanhamento)

        val resposta = acompanhamentoService.salvar(novoAcompanhamento)
        assertEquals(HttpStatus.CREATED.value(), resposta.statusCode.value())
        assertEquals(novoAcompanhamento, resposta.body)
    }

    @Test
    fun `deletar deve retornar 404 quando o acompanhamento não existe`() {
        `when`(acompanhamentoRepository.existsById(anyInt())).thenReturn(false)

        val resposta = acompanhamentoService.deletar(1)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `deletar deve deletar o acompanhamento e retornar 200 quando ele existe`() {
        `when`(acompanhamentoRepository.existsById(anyInt())).thenReturn(true)

        val resposta = acompanhamentoService.deletar(1)
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Acompanhamento(id = 1, Relatorio = "Relatorio 1", resumo = "Resumo 1", consulta = Consulta()), Acompanhamento(id = 2, Relatorio = "Relatorio 2", resumo = "Resumo 2", consulta = Consulta()))
        `when`(acompanhamentoRepository.findAll()).thenReturn(lista)

        val resultado = acompanhamentoService.getLista()
        assertEquals(2, resultado.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(acompanhamentoRepository.findAll()).thenReturn(emptyList())

        assertThrows<ResponseStatusException> {
            acompanhamentoService.getLista()
        }
    }
}