package br.com.dsena7.pix.model.dto;

import br.com.dsena7.pix.model.enums.InstituicoesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Builder
public class PixRequesDTO {

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotBlank
    private String chavePix;

    @NotNull
    private InstituicoesEnum instituicaoDeDestino;

    @NotBlank
    private String nomeDeDestinatario;

    public String getNomeDeDestinatario() {
        return nomeDeDestinatario;
    }

    public void setNomeDeDestinatario(String nomeDeDestinatario) {
        this.nomeDeDestinatario = nomeDeDestinatario;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public InstituicoesEnum getInstituicaoDeDestino() {
        return instituicaoDeDestino;
    }

    public void setInstituicaoDeDestino(InstituicoesEnum instituicaoDeDestino) {
        this.instituicaoDeDestino = instituicaoDeDestino;
    }
}

