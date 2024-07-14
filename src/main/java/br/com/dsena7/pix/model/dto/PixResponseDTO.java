package br.com.dsena7.pix.model.dto;

import br.com.dsena7.pix.model.enums.InstituicoesEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class PixResponseDTO {

    private String idDaTransacao;

    private BigDecimal valor;

    private String chavePix;

    private InstituicoesEnum instituicaoDeDestino;

    private String cpfCliente;

    private String nomeDeDestinatario;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cpfDestinatario;

    private String instituicao;
}
