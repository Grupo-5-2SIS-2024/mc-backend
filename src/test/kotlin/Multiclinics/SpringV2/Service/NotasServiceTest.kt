package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.dominio.Notas
import Multiclinics.SpringV2.repository.NotasRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class NotasServiceTest{
    private lateinit var notasRepository: NotasRepository
    private lateinit var notasService: NotasService

    @BeforeEach
    fun configurar() {
        notasRepository = mock(NotasRepository::class.java)
        notasService = NotasService(notasRepository)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val listaVazia = emptyList<Notas>()
        assertThrows(ResponseStatusException::class.java) {
            notasService.validarLista(listaVazia)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val listaNaoVazia = listOf(Notas(id = 1, titulo = "Nota 1", descricao = "Descricao 1", medico = Medico()))
        assertDoesNotThrow {
            notasService.validarLista(listaNaoVazia)
        }
    }

//    @Test
//    fun `salvar deve salvar a nota e retornar ResponseEntity com status 201`() {
//        val novaNota = Notas(id = 1, titulo = "Nota Nova", descricao = "Descricao Nova", medico = Medico())
//        `when`(notasRepository.save(any(Notas::class.java))).thenReturn(novaNota)
//
//        val resposta = notasService.salvar(novaNota)
//        assertEquals(HttpStatus.CREATED.value(), resposta.statusCode.value())
//        assertEquals(novaNota, resposta.body)
//    }

    @Test
    fun `atualizar deve retornar 404 quando a nota não existir`() {
        `when`(notasRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = notasService.atualizar(1, Notas(id = 1, titulo = "Atualizada", descricao = "Descricao Atualizada", medico = Medico()))
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 e atualizar a nota quando ela existir`() {
        val notaExistente = Notas(id = 1, titulo = "Existente", descricao = "Descricao Existente", medico = Medico())
        `when`(notasRepository.findById(anyInt())).thenReturn(Optional.of(notaExistente))
        `when`(notasRepository.save(any(Notas::class.java))).thenReturn(notaExistente)

        val resposta = notasService.atualizar(1, Notas(id = 1, titulo = "Atualizada", descricao = "Descricao Atualizada", medico = Medico()))
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals("Atualizada", resposta.body?.titulo)
        assertEquals("Descricao Atualizada", resposta.body?.descricao)
    }

    @Test
    fun `deletar deve lançar exceção quando a nota não existir`() {
        `when`(notasRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            notasService.deletar(1)
        }
    }

    @Test
    fun `deletar deve chamar deleteById quando a nota existir`() {
        `when`(notasRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            notasService.deletar(1)
        }
        verify(notasRepository, times(1)).deleteById(1)
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Notas(id = 1, titulo = "Nota 1", descricao = "Descricao 1", medico = Medico()),
            Notas(id = 2, titulo = "Nota 2", descricao = "Descricao 2", medico = Medico()))
        `when`(notasRepository.findAll()).thenReturn(lista)

        val resultado = notasService.getLista()
        assertEquals(2, resultado.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(notasRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            notasService.getLista()
        }
    }
}