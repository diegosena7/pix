package br.com.dsena7.pix.utils;

import br.com.dsena7.pix.exceptions.ExcecoesNegocios;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.entity.TransacaoEntity;
import br.com.dsena7.pix.model.mapper.TransacoesMapper;
import br.com.dsena7.pix.repository.TransacaoRepository;
import br.com.dsena7.pix.service.TransacaoProducer;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

public class TransactionUtils {
    public static TransacaoEntity checkAndLockTransaction(TransacaoEntity transacaoEntity, TransacaoRepository transacaoRepository) throws ExcecoesNegocios {
        TransacaoEntity existingTransacao = transacaoRepository.findByIdDaTransacao(transacaoEntity.getIdDaTransacao());
        if (existingTransacao != null) {
            if (Boolean.TRUE.equals(existingTransacao.getTransacaoProcessada())) {
                throw new ExcecoesNegocios("A transação já está em processamento.");
            }
            // Marcar como bloqueado
            existingTransacao.setTransacaoProcessada(true);
            transacaoRepository.save(existingTransacao);
        }
        return existingTransacao;
    }

    public static TransacaoEntity updateExistingTransacao(TransacaoEntity existingTransacao, PixRequesDTO pixRequesDTO) throws ExcecoesNegocios {
        if (Boolean.TRUE.equals(existingTransacao.getTransacaoProcessada())) {
            throw new ExcecoesNegocios("A transação já foi processada.");
        }
        existingTransacao = TransacoesMapper.toTransacaoEntity(pixRequesDTO);
        return existingTransacao;
    }

    public static void saveTransacao(TransacaoEntity transacaoEntity, TransacaoRepository transacaoRepository) {
        transacaoRepository.save(transacaoEntity);
    }

    public static void sendTransactionMessageWithCircuitBreaker(PixRequesDTO pixRequesDTO, CircuitBreaker circuitBreaker, TransacaoProducer transacaoProducer) {
        CircuitBreaker.decorateRunnable(circuitBreaker, () -> transacaoProducer.sendTransactionMessage(pixRequesDTO)).run();
    }

    public static void markTransactionAsProcessed(TransacaoEntity transacaoEntity, TransacaoRepository transacaoRepository) {
        transacaoEntity.setTransacaoProcessada(Boolean.TRUE);
        transacaoRepository.save(transacaoEntity);
    }
}
