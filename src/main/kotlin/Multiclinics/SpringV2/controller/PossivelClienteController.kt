package Multiclinics.SpringV2.controller

import Multiclinics.SpringV2.dominio.PossivelCliente
import Multiclinics.SpringV2.Service.PossivelClienteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/possivel-cliente")
class PossivelClienteController(
    private val possivelClienteService: PossivelClienteService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<PossivelCliente>> {
        val clientes = possivelClienteService.listarTodos()
        return ResponseEntity.ok(clientes)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<PossivelCliente> {
        val cliente = possivelClienteService.buscarPorId(id)
        return ResponseEntity.ok(cliente)
    }

    @PostMapping
    fun criar(@RequestBody possivelCliente: PossivelCliente): ResponseEntity<PossivelCliente> {
        val clienteCriado = possivelClienteService.criar(possivelCliente)
        return ResponseEntity.status(201).body(clienteCriado)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody possivelCliente: PossivelCliente): ResponseEntity<PossivelCliente> {
        val clienteAtualizado = possivelClienteService.atualizar(id, possivelCliente)
        return ResponseEntity.ok(clienteAtualizado)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        possivelClienteService.deletar(id)
        return ResponseEntity.noContent().build()
    }
}