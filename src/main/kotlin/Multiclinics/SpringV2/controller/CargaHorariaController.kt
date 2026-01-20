package Multiclinics.SpringV2.controller
import Multiclinics.SpringV2.Service.CargaHorariaService
import Multiclinics.SpringV2.dominio.CargaHoraria
import Multiclinics.SpringV2.dto.CargaHorariaRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/carga-horaria")
class CargaHorariaController(
    val service: CargaHorariaService
) {

    @PostMapping("/medico/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun cadastrar(
        @PathVariable id: Int,
        @RequestBody lista: List<CargaHorariaRequest>
    ) {
        service.salvarEmLoteDto(id, lista)
    }


    @GetMapping("/medico/{id}")
    fun listar(@PathVariable id: Int): List<CargaHoraria> {
        return service.listarPorMedico(id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable id: Int) {
        service.deletar(id)
    }
}