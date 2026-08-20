package Multiclinics.SpringV2.Service
import Multiclinics.SpringV2.dominio.PossivelCliente
import Multiclinics.SpringV2.repository.PossivelClienteRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class PossivelClienteService(
    private val possivelClienteRepository: PossivelClienteRepository
) {

    fun listarTodos(): List<PossivelCliente> {
        return possivelClienteRepository.findAll()
    }

    fun buscarPorId(id: Int): PossivelCliente {
        return possivelClienteRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
        }
    }

    fun criar(possivelCliente: PossivelCliente): PossivelCliente {
        possivelCliente.dataInsercao = LocalDateTime.now()
        return possivelClienteRepository.save(possivelCliente)
    }

    fun atualizar(id: Int, possivelCliente: PossivelCliente): PossivelCliente {
        if (!possivelClienteRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
        }
        possivelCliente.id = id
        return possivelClienteRepository.save(possivelCliente)
    }

    fun deletar(id: Int) {
        if (!possivelClienteRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado")
        }
        possivelClienteRepository.deleteById(id)
    }
}