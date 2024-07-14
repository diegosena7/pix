package br.com.dsena7.pix.service;

import br.com.dsena7.pix.exceptions.ExcecoesNegocios;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.entity.TransacaoEntity;
import br.com.dsena7.pix.repository.TransacaoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransacaoServiceIntegrationTest {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @AfterEach
    void tearDown() {
        transacaoRepository.deleteAll();
    }

    @Test
    void insereTransacao_shouldSaveTransaction() throws ExcecoesNegocios {
        PixRequesDTO dto = PixRequesDTO.builder()
                .valor(new BigDecimal(1500))
                .nomeDeDestinatario("Doug")
                .chavePix("chavePix")
                .build();

        transacaoService.insereTransacao(dto);

        List<TransacaoEntity> savedList = transacaoRepository.findAll();
        assert !savedList.isEmpty();
        assert savedList.get(0).getChavePix().equals(dto.getChavePix());
    }
}
