package Multiclinics.SpringV2.repository
import Multiclinics.SpringV2.dominio.PossivelCliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PossivelClienteRepository : JpaRepository<PossivelCliente, Int>




