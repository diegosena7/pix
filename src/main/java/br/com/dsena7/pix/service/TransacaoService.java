package br.com.dsena7.pix.service;

import br.com.dsena7.pix.exceptions.ExcecoesNegocios;
import br.com.dsena7.pix.model.mapper.TransacoesMapper;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.dto.PixResponseDTO;
import br.com.dsena7.pix.model.entity.TransacaoEntity;
import br.com.dsena7.pix.repository.TransacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

    public void insereTransacao(PixRequesDTO pixRequesDTO) throws ExcecoesNegocios {
        TransacaoEntity transacaoEntity = TransacoesMapper.toTransacaoEntity(pixRequesDTO);
        try {
            transacaoRepository.save(transacaoEntity);
            transacaoProducer.sendTransactionMessage(pixRequesDTO);
        } catch (OptimisticLockingFailureException exception) {
            throw new ExcecoesNegocios("Conflito de concorrência detectado: " + exception.getMessage());
        }
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
