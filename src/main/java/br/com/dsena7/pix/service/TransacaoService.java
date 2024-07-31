package br.com.dsena7.pix.service;

import br.com.dsena7.pix.exceptions.ExcecoesNegocios;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.dto.PixResponseDTO;
import br.com.dsena7.pix.model.entity.TransacaoEntity;
import br.com.dsena7.pix.model.mapper.TransacoesMapper;
import br.com.dsena7.pix.repository.TransacaoRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_PREFIX = "TRANSACAO_";

    @Autowired
    private TransacaoProducer transacaoProducer;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @Autowired
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("sendTransactionMessage");
    }

    @Transactional
    public void insereTransacao(PixRequesDTO pixRequesDTO) throws ExcecoesNegocios {
        TransacaoEntity transacaoEntity = TransacoesMapper.toTransacaoEntity(pixRequesDTO);

        try {
            // Verifica e bloqueia a transação existente
            TransacaoEntity existingTransacao = checkAndLockTransacao(transacaoEntity);

            if (existingTransacao != null) {
                transacaoEntity = updateExistingTransacao(existingTransacao, pixRequesDTO);
            }

            // Salva a transação
            saveTransacao(transacaoEntity);

            // Protege a chamada ao RabbitMQ com Circuit Breaker
            sendTransactionMessageWithCircuitBreaker(pixRequesDTO);

            // Marca a transação como processada e salva novamente
            markTransacaoAsProcessed(transacaoEntity);

        } catch (OptimisticLockingFailureException exception) {
            throw new ExcecoesNegocios("Conflito de concorrência detectado: " + exception.getMessage());
        } catch (Exception e) {
            log.error("Erro ao processar a transação: ", e);
            throw new ExcecoesNegocios("Erro ao processar a transação: " + e.getMessage());
        }
    }

    private TransacaoEntity checkAndLockTransacao(TransacaoEntity transacaoEntity) throws ExcecoesNegocios {
        TransacaoEntity existingTransacao = transacaoRepository.findByIdDaTransacao(transacaoEntity.getIdDaTransacao());
        if (existingTransacao != null && Boolean.TRUE.equals(existingTransacao.getTransacaoProcessada())) {
            throw new ExcecoesNegocios("A transação já foi processada.");
        }
        return existingTransacao;
    }

    private TransacaoEntity updateExistingTransacao(TransacaoEntity existingTransacao, PixRequesDTO pixRequesDTO) throws ExcecoesNegocios {
        if (Boolean.TRUE.equals(existingTransacao.getTransacaoProcessada())) {
            throw new ExcecoesNegocios("A transação já foi processada.");
        }
        existingTransacao = TransacoesMapper.toTransacaoEntity(pixRequesDTO);
        return existingTransacao;
    }

    private void saveTransacao(TransacaoEntity transacaoEntity) {
        transacaoRepository.save(transacaoEntity);
    }

    private void sendTransactionMessageWithCircuitBreaker(PixRequesDTO pixRequesDTO) {
        CircuitBreaker.decorateRunnable(circuitBreaker, () -> transacaoProducer.sendTransactionMessage(pixRequesDTO)).run();
    }

    private void markTransacaoAsProcessed(TransacaoEntity transacaoEntity) {
        transacaoEntity.setTransacaoProcessada(Boolean.TRUE);
        transacaoRepository.save(transacaoEntity);
    }

    public List<PixResponseDTO> buscaTransacoesPorCpf(String cpf) throws ExcecoesNegocios {
        List<TransacaoEntity> transacoes = transacaoRepository.findByCpfCliente(cpf);
        if (transacoes.isEmpty()) {
            throw new ExcecoesNegocios("Transacões não encontradas para o cpf informado.");
        }
        return TransacoesMapper.toPixResponseDTOs(transacoes);
    }

    public PixResponseDTO buscarTransacaoPorIdDaTransacao(String idDaTransacao) throws ExcecoesNegocios {
        String redisKey = REDIS_PREFIX + idDaTransacao;
        PixResponseDTO transacaoCached = (PixResponseDTO) redisTemplate.opsForValue().get(redisKey);

        if (transacaoCached != null) {
            Object value = redisTemplate.opsForValue().get(redisKey);
            log.info("-- Redis object... value: " + value + " in the key:" + redisKey);
            return transacaoCached;
        }

        TransacaoEntity transacao = transacaoRepository.findByIdDaTransacao(idDaTransacao);
        if (transacao == null) {
            throw new ExcecoesNegocios("Transação não encontrada para o ID da transação informado.");
        }

        PixResponseDTO transacaoDto = TransacoesMapper.toPixResponseDTO(transacao);
        redisTemplate.opsForValue().set(redisKey, transacaoDto, 10, TimeUnit.MINUTES); // Cache por 10 minutos

        return transacaoDto;
    }
}
