package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.dominio.Medico
import Multiclinics.SpringV2.repository.ConsultaRepository
import Multiclinics.SpringV2.repository.MedicoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*


class ConsultaServiceTest{
    private lateinit var consultaRepository: ConsultaRepository
    private lateinit var medicoRepository: MedicoRepository
    private lateinit var consultaService: ConsultaService

    @BeforeEach
    fun configurar() {
        consultaRepository = mock(ConsultaRepository::class.java)
        medicoRepository = mock(MedicoRepository::class.java)
        consultaService = ConsultaService(consultaRepository, medicoRepository)
    }

    @Test
    fun `validarLista deve lançar exceção quando a lista estiver vazia`() {
        val listaVazia = emptyList<Consulta>()
        assertThrows(ResponseStatusException::class.java) {
            consultaService.validarLista(listaVazia)
        }
    }

    @Test
    fun `validarLista não deve lançar exceção quando a lista não estiver vazia`() {
        val listaNaoVazia = listOf(Consulta(id = 1, descricao = "Consulta 1", medico = Medico()))
        assertDoesNotThrow {
            consultaService.validarLista(listaNaoVazia)
        }
    }

    @Test
    fun `atualizar deve retornar 404 quando o medico não existir`() {
        val novaConsulta = Consulta(id = 1, descricao = "Atualizada", medico = Medico(id = 1))
        `when`(medicoRepository.existsById(anyInt())).thenReturn(false)

        val resposta = consultaService.atualizar(1, novaConsulta)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 404 quando a consulta não existir`() {
        val novaConsulta = Consulta(id = 1, descricao = "Atualizada", medico = Medico(id = 1))
        `when`(medicoRepository.existsById(anyInt())).thenReturn(true)
        `when`(consultaRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = consultaService.atualizar(1, novaConsulta)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `atualizar deve retornar 200 e atualizar a consulta quando ela existir`() {
        val consultaExistente = Consulta(id = 1, descricao = "Existente", medico = Medico(id = 1))
        val novaConsulta = Consulta(id = 1, descricao = "Atualizada", medico = Medico(id = 1))
        `when`(medicoRepository.existsById(anyInt())).thenReturn(true)
        `when`(consultaRepository.findById(anyInt())).thenReturn(Optional.of(consultaExistente))
        `when`(consultaRepository.save(any(Consulta::class.java))).thenReturn(novaConsulta)

        val resposta = consultaService.atualizar(1, novaConsulta)
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals(novaConsulta.descricao, resposta.body?.descricao)
    }

    @Test
    fun `salvar deve lançar exceção quando o medico não existir`() {
        val novaConsulta = Consulta(id = 1, descricao = "Nova", medico = Medico(id = 1))
        `when`(medicoRepository.existsById(anyInt())).thenReturn(false)

        assertThrows(ResponseStatusException::class.java) {
            consultaService.salvar(novaConsulta)
        }
    }

    @Test
    fun `salvar deve salvar a consulta quando o medico existir`() {
        val novaConsulta = Consulta(id = 1, descricao = "Nova", medico = Medico(id = 1))
        `when`(medicoRepository.existsById(anyInt())).thenReturn(true)
        `when`(consultaRepository.save(any(Consulta::class.java))).thenReturn(novaConsulta)

        val resposta = consultaService.salvar(novaConsulta)
        assertEquals(novaConsulta, resposta)
    }

    @Test
    fun `deletar deve retornar 404 quando a consulta não existir`() {
        `when`(consultaRepository.findById(anyInt())).thenReturn(Optional.empty())

        val resposta = consultaService.deletar(1)
        assertEquals(HttpStatus.NOT_FOUND.value(), resposta.statusCode.value())
    }

    @Test
    fun `deletar deve retornar 200 e deletar a consulta quando ela existir`() {
        val consultaExistente = Consulta(id = 1, descricao = "Existente", medico = Medico(id = 1))
        `when`(consultaRepository.findById(anyInt())).thenReturn(Optional.of(consultaExistente))

        val resposta = consultaService.deletar(1)
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        verify(consultaRepository, times(1)).delete(consultaExistente)
    }

    @Test
    fun `getLista deve retornar lista quando não estiver vazia`() {
        val lista = listOf(Consulta(id = 1, descricao = "Consulta 1", medico = Medico()),
            Consulta(id = 2, descricao = "Consulta 2", medico = Medico()))
        `when`(consultaRepository.findAll()).thenReturn(lista)

        val resultado = consultaService.getLista()
        assertEquals(2, resultado.size)
    }

    @Test
    fun `getLista deve lançar exceção quando a lista estiver vazia`() {
        `when`(consultaRepository.findAll()).thenReturn(emptyList())

        assertThrows(ResponseStatusException::class.java) {
            consultaService.getLista()
        }
    }

//    @Test
//    fun `getTop3ConsultasByData deve retornar top 3 consultas ordenadas por data`() {
//        val consultas = listOf(
//            arrayOf("Paciente 1", "2024-06-01", "Especialidade 1"),
//            arrayOf("Paciente 2", "2024-05-01", "Especialidade 2"),
//            arrayOf("Paciente 3", "2024-04-01", "Especialidade 3")
//        )
//        `when`(consultaRepository.findTop3ByOrderByDatahoraConsultaDesc()).thenReturn(consultas)
//
//        val resultado = consultaService.getTop3ConsultasByData()
//        assertEquals(3, resultado.size)
//        assertEquals("Paciente 1", resultado[0]["nomePaciente"])
//    }

    @Test
    fun `getListaNome deve retornar lista de consultas por nome do medico`() {
        val consultasMedico = listOf(Consulta(id = 1, descricao = "Consulta 1", medico = Medico(nome = "Dr. João")),
            Consulta(id = 2, descricao = "Consulta 2", medico = Medico(nome = "Dr. João")))
        `when`(consultaRepository.findByMedicoNome("Dr. João")).thenReturn(consultasMedico)

        val resposta = consultaService.getListaNome("Dr. João")
        assertEquals(HttpStatus.OK.value(), resposta.statusCode.value())
        assertEquals(2, resposta.body?.size)
    }

//    @Test
//    fun `getAltasUltimosSeisMeses deve retornar altas dos ultimos seis meses`() {
//        val result = listOf(arrayOf("Jan", 10), arrayOf("Feb", 20))
//        `when`(consultaRepository.findAltasUltimosSeisMeses()).thenReturn(result)
//
//        val resposta = consultaService.getAltasUltimosSeisMeses()
//        assertEquals(2, resposta.size)
//        assertEquals("Jan", resposta[0]["mes"])
//        assertEquals(10, resposta[0]["total"])
//    }
//
//    @Test
//    fun `getHorariosUltimosSeisMeses deve retornar horarios dos ultimos seis meses`() {
//        val result = listOf(arrayOf("Jan", 30, 40), arrayOf("Feb", 20, 50))
//        `when`(consultaRepository.findHorariosUltimosSeisMeses()).thenReturn(result)
//
//        val resposta = consultaService.getHorariosUltimosSeisMeses()
//        assertEquals(2, resposta.size)
//        assertEquals("Jan", resposta[0]["mes"])
//        assertEquals(30, resposta[0]["agendados"])
//        assertEquals(40, resposta[0]["disponiveis"])
//    }

    @Test
    fun `getPercentagemConcluidos deve retornar percentagem de consultas concluidas`() {
        `when`(consultaRepository.countTotal()).thenReturn(100)
        `when`(consultaRepository.countConcluidos()).thenReturn(80)

        val resposta = consultaService.getPercentagemConcluidos()
        assertEquals(80.0, resposta)
    }
}