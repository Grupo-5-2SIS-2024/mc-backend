package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Endereco
import Multiclinics.SpringV2.repository.EnderecoRespository
import org.springframework.stereotype.Service

@Service
class EnderecoService(
    val enderecoRespository: EnderecoRespository
) {

    fun criar(endereco: Endereco) : Endereco?{
        return enderecoRespository.save(endereco)
    }
}