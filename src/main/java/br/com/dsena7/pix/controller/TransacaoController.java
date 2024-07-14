package br.com.dsena7.pix.controller;

import br.com.dsena7.pix.exceptions.ExcecoesNegocios;
import br.com.dsena7.pix.model.dto.ErroResponseDTO;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.dto.PixResponseDTO;
import br.com.dsena7.pix.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/transacao")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/pix")
    public ResponseEntity<String> inserePix(@RequestBody @Valid PixRequesDTO pixRequesDTO) throws ExcecoesNegocios {
        transacaoService.insereTransacao(pixRequesDTO);
        return ResponseEntity.ok("Transação realizada com sucesso");
    }

    @GetMapping("/pix/cpf/{cpf}")
    public ResponseEntity<?> buscaTransacoesPorCpf(@PathVariable String cpf) {
        try {
            List<PixResponseDTO> transacoes = transacaoService.buscaTransacoesPorCpf(cpf);
            return ResponseEntity.ok(transacoes);
        } catch (ExcecoesNegocios e) {
            ErroResponseDTO errorResponse = new ErroResponseDTO("Transações não encontradas para o CPF fornecido.", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/pix/id/{idDTransacao}")
    public ResponseEntity<?> buscarTransacaoPorIdDaTransacao(@PathVariable String idDTransacao){
        try {
            PixResponseDTO transacao = transacaoService.buscarTransacaoPorIdDaTransacao(idDTransacao);
            return ResponseEntity.ok(transacao);
        } catch (ExcecoesNegocios e) {
            ErroResponseDTO errorResponse = new ErroResponseDTO("Transacao não encontrada para o id da transação informado: " + idDTransacao, HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
