package br.com.dsena7.pix.service;

import br.com.dsena7.pix.exceptions.ExcecoesNegocios;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.entity.TransacaoEntity;
import br.com.dsena7.pix.model.mapper.TransacoesMapper;
import br.com.dsena7.pix.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.OptimisticLockingFailureException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoProducer transacaoProducer;

    public TransacaoServiceTest() {
        openMocks(this);
    }
    @Test
    void insereTransacao_shouldSaveTransaction() throws ExcecoesNegocios {
        PixRequesDTO dto = PixRequesDTO.builder()
                .valor(new BigDecimal(1500))
                .nomeDeDestinatario("Doug")
                .chavePix("chavePix")
                .build();
        transacaoService.insereTransacao(dto);
        verify(transacaoRepository).save(any(TransacaoEntity.class));
    }

    @Test
    void insereTransacao_shouldThrowExceptionOnOptimisticLockingFailure() {
        PixRequesDTO dto = PixRequesDTO.builder()
                .valor(new BigDecimal(1500))
                .nomeDeDestinatario("Doug")
                .chavePix("chavePix")
                .build();

        // Configuração do mock para lançar a exceção de conflito de versão
        doThrow(new OptimisticLockingFailureException("Conflito de concorrência")).when(transacaoRepository).save(any(TransacaoEntity.class));

        // Verifica se a exceção é lançada corretamente
        ExcecoesNegocios exception = assertThrows(ExcecoesNegocios.class, () -> transacaoService.insereTransacao(dto));

        // Verifica a mensagem da exceção
        assertEquals("Conflito de concorrência detectado: Conflito de concorrência", exception.getMessage());

        // Verifica se o método save foi chamado no repositório
        verify(transacaoRepository).save(any(TransacaoEntity.class));
    }
}
