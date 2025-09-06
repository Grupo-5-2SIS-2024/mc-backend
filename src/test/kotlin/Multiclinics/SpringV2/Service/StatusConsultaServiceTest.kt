package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.StatusConsulta
import Multiclinics.SpringV2.repository.StatusRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class StatusConsultaServiceTest{
    private lateinit var statusRepository: StatusRepository
    private lateinit var statusConsultaService: StatusConsultaService

    @BeforeEach
    fun setUp() {
        statusRepository = mock(StatusRepository::class.java)
        statusConsultaService = StatusConsultaService(statusRepository)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val emptyList = emptyList<StatusConsulta>()
        assertThrows(ResponseStatusException::class.java) {
            statusConsultaService.validarLista(emptyList)
        }
    }
//    @Test
//    fun `validarLista should not throw exception when list is not empty`() {
//        val nonEmptyList = listOf((StatusConsulta(id = 1, nomeStatus = "status1"))
//                assertDoesNotThrow {
//            statusConsultaService.validarLista(nonEmptyList)
//        }
//    }

    @Test
    fun `salvar deve retornar 401 quando a permissão já existir`() {
        val novaStatus = StatusConsulta(id = 1,nomeStatus = "existing")
        `when`(statusRepository.findByNomeStatus(anyString())).thenReturn(novaStatus)

        val response = statusConsultaService.salvar(novaStatus)
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode.value())
    }

    @Test
    fun `salvar deve retornar 201 quando a permissão não existir`() {
        val novaStatus = StatusConsulta(id = 1,nomeStatus = "new")
        `when`(statusRepository.findByNomeStatus(anyString())).thenReturn(null)
        `when`(statusRepository.save(any(StatusConsulta::class.java))).thenReturn(novaStatus)

        val response = statusConsultaService.salvar(novaStatus)
        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(novaStatus, response.body)
    }

    @Test
    fun `atualizar deve retornar 404 quando a permissão não existir`() {
        `when`(statusRepository.findById(anyInt())).thenReturn(Optional.empty())

        val response = statusConsultaService.atualizar(1, StatusConsulta(id = 1,nomeStatus = "update"))
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 quando a permissão existir`() {
        val existingStatus = StatusConsulta(id = 1,nomeStatus = "existing")
        `when`(statusRepository.findById(anyInt())).thenReturn(Optional.of(existingStatus))
        `when`(statusRepository.save(any(StatusConsulta::class.java))).thenReturn(existingStatus)

        val response = statusConsultaService.atualizar(1, StatusConsulta(id = 1,nomeStatus = "updated"))
        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals("updated", response.body?.nomeStatus)
    }

    @Test
    fun `deletar deve lançar exceção quando a permissão não existir`() {
        `when`(statusRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            statusConsultaService.deletar(1)
        }
    }

    @Test
    fun `deletar deve chamar deleteById quando a permissão existir`() {
        `when`(statusRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            statusConsultaService.deletar(1)
        }
        verify(statusRepository, times(1)).deleteById(1)
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(StatusConsulta(id = 1,nomeStatus = "status1"), StatusConsulta(id = 1,nomeStatus = "status2"))
        `when`(statusRepository.findAll()).thenReturn(lista)

        val result = statusConsultaService.getLista()
        assertEquals(2, result.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(statusRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            statusConsultaService.getLista()
        }
    }
}
