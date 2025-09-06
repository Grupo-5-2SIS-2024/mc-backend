package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Permissionamento
import Multiclinics.SpringV2.repository.PermissionamentoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

class PermissionamentoServiceTest{
    private lateinit var permissionamentoRepository: PermissionamentoRepository
    private lateinit var permissionamentoService: PermissionamentoService

    @BeforeEach
    fun setUp() {
        permissionamentoRepository = mock(PermissionamentoRepository::class.java)
        permissionamentoService = PermissionamentoService(permissionamentoRepository)
    }

    @Test
    fun `validarLista deve jogar exception quando list é vazia`() {
        val emptyList = emptyList<Permissionamento>()
        assertThrows(ResponseStatusException::class.java) {
            permissionamentoService.validarLista(emptyList)
        }
    }

    @Test
    fun `validarLista não deve jogar exception quando list não é vazia`() {
        val nonEmptyList = listOf(Permissionamento(id = 1, nome = "Admin"))
        assertDoesNotThrow {
            permissionamentoService.validarLista(nonEmptyList)
        }
    }

    @Test
    fun `salvar deve retornar 401 quando a permissão já existir`() {
        val novaPermissao = Permissionamento(id = 1, nome = "existing")
        `when`(permissionamentoRepository.findByNome(anyString())).thenReturn(novaPermissao)

        val response = permissionamentoService.salvar(novaPermissao)
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.statusCode.value())
    }

    @Test
    fun `salvar deve retornar 201 quando a permissão não existir`() {
        val novaPermissao = Permissionamento(id = 1, nome = "new")
        `when`(permissionamentoRepository.findByNome(anyString())).thenReturn(null)
        `when`(permissionamentoRepository.save(any(Permissionamento::class.java))).thenReturn(novaPermissao)

        val response = permissionamentoService.salvar(novaPermissao)
        assertEquals(HttpStatus.CREATED.value(), response.statusCode.value())
        assertEquals(novaPermissao, response.body)
    }

    @Test
    fun `atualizar deve retornar 404 quando a permissão não existir`() {
        `when`(permissionamentoRepository.findById(anyInt())).thenReturn(Optional.empty())

        val response = permissionamentoService.atualizar(1, Permissionamento(id = 1, nome = "update"))
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 quando a permissão existir`() {
        val existingPermissao = Permissionamento(id = 1, nome = "existing")
        `when`(permissionamentoRepository.findById(anyInt())).thenReturn(Optional.of(existingPermissao))
        `when`(permissionamentoRepository.save(any(Permissionamento::class.java))).thenReturn(existingPermissao)

        val response = permissionamentoService.atualizar(1, Permissionamento(id = 1, nome = "updated"))
        assertEquals(HttpStatus.OK.value(), response.statusCode.value())
        assertEquals("updated", response.body?.nome)
    }

    @Test
    fun `deletar deve lançar exceção quando a permissão não existir`() {
        `when`(permissionamentoRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            permissionamentoService.deletar(1)
        }
    }

    @Test
    fun `deletar deve chamar deleteById quando a permissão existir`() {
        `when`(permissionamentoRepository.existsById(anyInt())).thenReturn(true)

        assertDoesNotThrow {
            permissionamentoService.deletar(1)
        }
        verify(permissionamentoRepository, times(1)).deleteById(1)
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Permissionamento(id = 1, nome = "Admin"), Permissionamento(id = 2, nome = "User"))
        `when`(permissionamentoRepository.findAll()).thenReturn(lista)

        val result = permissionamentoService.getLista()
        assertEquals(2, result.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(permissionamentoRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            permissionamentoService.getLista()
        }
    }
}