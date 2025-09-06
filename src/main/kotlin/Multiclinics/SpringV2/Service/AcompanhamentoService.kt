package Multiclinics.SpringV2.Service

import Multiclinics.SpringV2.dominio.Acompanhamento
import Multiclinics.SpringV2.dominio.Consulta
import Multiclinics.SpringV2.repository.AcompanhamentoRepository
import Multiclinics.SpringV2.repository.ConsultaRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.io.FileReader
import java.util.*

@Service
class AcompanhamentoService(
    val acompanhamentoRepository: AcompanhamentoRepository,
    val consultaRepository: ConsultaRepository
) {
    fun validarLista(lista:List<*>) {
        if (lista.isEmpty()) {
            throw ResponseStatusException(HttpStatusCode.valueOf(204))
        }
    }

    fun atualizar(id: Int ,novoAcompanhamento: Acompanhamento): ResponseEntity<Acompanhamento> {
        val AcompanhamentoExistente = acompanhamentoRepository.findById(id)
        if (AcompanhamentoExistente.isPresent) {
            val AcompanhamentoEscolhido = AcompanhamentoExistente.get()

            // Atualiza os dados do médico existente com os novos dados

            AcompanhamentoEscolhido.Relatorio = novoAcompanhamento.Relatorio
            AcompanhamentoEscolhido.resumo = novoAcompanhamento.resumo




            val AcompanhamentoAtualizado = acompanhamentoRepository.save(AcompanhamentoEscolhido)
            return ResponseEntity.status(200).body(AcompanhamentoAtualizado)
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    fun salvar(novoAcompanhamento: Acompanhamento): ResponseEntity<Acompanhamento> {
        acompanhamentoRepository.save(novoAcompanhamento)
        return ResponseEntity.status(201).body(novoAcompanhamento)
    }

    fun deletar(id: Int): ResponseEntity<Any> {
        if (acompanhamentoRepository.existsById(id)) {
            acompanhamentoRepository.deleteById(id)
            return ResponseEntity.status(200).build()
        }
        return ResponseEntity.status(404).build()

    }


    fun getLista():List<Acompanhamento> {
        val lista = acompanhamentoRepository.findAll()
        validarLista(lista)

        return lista
    }

    fun  lerArquivoTxt(arquivo: String, fkConsulta: Int){

        var leitor = Scanner(arquivo)
        val consulta = consultaRepository.findById(fkConsulta).get()
        val listaFeedbacks = mutableListOf<Acompanhamento>()

        while(leitor.hasNext()){
            val linha = leitor.nextLine()
            var registro = linha.substring(0, 2)
            if(registro == "00"){
                val conteudo = linha.substring(2, 10)
                val dataHora = linha.substring(10, 29)
                val versao = linha.substring(29, 31)
                println("Conteudo do arquivo: $conteudo")
                println("dataHora: $dataHora")
                println("versao: $versao")
            }else if(registro == "02"){
                val id = linha.substring(2,7).toInt()
                val resumo = linha.substring(7,52).trim()
                val Relatorio = linha.substring(52,97).trim()
                val feedback = Acompanhamento(id, resumo, Relatorio,consulta)
                acompanhamentoRepository.save(feedback)
                listaFeedbacks.add(feedback)
            }else if(registro == "01"){
                val qtdRegistros = linha.substring(2,12).toInt()

                if(qtdRegistros == listaFeedbacks.size){
                    println("Quantidade de registros corresponde ao valor informado de $qtdRegistros")
                }else{
                    println("Quantidade de registros NÃO corresponde ao valor informado de $qtdRegistros")
                }
            }

        }
        leitor.close()
        println("Leitura do TXT bem sucedida")
    }

}