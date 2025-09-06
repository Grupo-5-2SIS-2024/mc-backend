package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.EspecificacaoMedica
import Multiclinics.SpringV2.repository.EspecificacaoMedicaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class EspecificacaoMedicaServiceTest{
    private lateinit var especificacaoRepository: EspecificacaoMedicaRepository
    private lateinit var especificacaoMedicaService: EspecificacaoMedicaService

    @BeforeEach
    fun setUp() {
        especificacaoRepository = mock(EspecificacaoMedicaRepository::class.java)
        especificacaoMedicaService = EspecificacaoMedicaService(especificacaoRepository)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val emptyList = emptyList<EspecificacaoMedica>()
        assertThrows(ResponseStatusException::class.java) {
            especificacaoMedicaService.validarLista(emptyList)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val nonEmptyList = listOf(EspecificacaoMedica(id = 1, area = "Cardiology"))
        assertDoesNotThrow {
            especificacaoMedicaService.validarLista(nonEmptyList)
        }
    }

    @Test
    fun `salvar deve retornar 401 quando a permissão já existir`() {
        val novaEspecificacao = EspecificacaoMedica(id = 1, area = "existing")
        `when`(especificacaoRepository.findByArea(anyString())).thenReturn(novaEspecificacao)

        val response = especificacaoMedicaService.salvar(novaEspecificacao)
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode.value())
    }

    @Test
    fun `salvar deve retornar 201 quando a permissão não existir`() {
        val novaEspecificacao = EspecificacaoMedica(id = 1, area = "new")
        `when`(especificacaoRepository.findByArea(anyString())).thenReturn(null)
        `when`(especificacaoRepository.save(any(EspecificacaoMedica::class.java))).thenReturn(novaEspecificacao)

        val response = especificacaoMedicaService.salvar(novaEspecificacao)
        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(novaEspecificacao, response.body)
    }

    @Test
    fun `atualizar deve retornar 404 quando a permissão não existir`() {
        `when`(especificacaoRepository.findById(anyInt())).thenReturn(Optional.empty())

        val response = especificacaoMedicaService.atualizar(1, EspecificacaoMedica(id = 1, area = "update"))
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 quando a permissão existir`() {
        val existingEspecificacao = EspecificacaoMedica(id = 1, area = "existing")
        `when`(especificacaoRepository.findById(anyInt())).thenReturn(Optional.of(existingEspecificacao))
        `when`(especificacaoRepository.save(any(EspecificacaoMedica::class.java))).thenReturn(existingEspecificacao)

        val response = especificacaoMedicaService.atualizar(1, EspecificacaoMedica(id = 1, area = "updated"))
        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals("updated", response.body?.area)
    }

    @Test
    fun `deletar deve lançar exceção quando a permissão não existir`() {
        `when`(especificacaoRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            especificacaoMedicaService.deletar(1)
        }
    }

    @Test
    fun `deletar deve chamar deleteById quando a permissão existir`() {
        `when`(especificacaoRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            especificacaoMedicaService.deletar(1)
        }
        verify(especificacaoRepository, times(1)).deleteById(1)
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(EspecificacaoMedica(id = 1, area = "Cardiology"), EspecificacaoMedica(id = 2, area = "Dermatology"))
        `when`(especificacaoRepository.findAll()).thenReturn(lista)

        val result = especificacaoMedicaService.getLista()
        assertEquals(2, result.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(especificacaoRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            especificacaoMedicaService.getLista()
        }
    }
}