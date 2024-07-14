package br.com.dsena7.pix.model.entity;

import br.com.dsena7.pix.model.enums.InstituicoesEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao_consumer_pix")
@Builder
public class TransacaoConsumerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idDaTransacao;

    @Positive
    private BigDecimal valor;

    private String chavePix;

    @Enumerated(EnumType.STRING)
    private InstituicoesEnum instituicaoReceptora;

    @Builder.Default
    private String cpfCliente = "teste-cpf";

    @Builder.Default
    private String instituicaoTransmissora = "Banco do Diego";

    private String nomeDeDestinatario;

    @Builder.Default
    private LocalDateTime dataDaTrasacao = LocalDateTime.now();

    public LocalDateTime getDataDaTrasacao() {
        return dataDaTrasacao;
    }

    public void setDataDaTrasacao(LocalDateTime dataDaTrasacao) {
        this.dataDaTrasacao = dataDaTrasacao;
    }

    public String getIdDaTransacao() {
        return idDaTransacao;
    }

    public void setIdDaTransacao(String idDaTransacao) {
        this.idDaTransacao = idDaTransacao;
    }

    public String getNomeDeDestinatario() {
        return nomeDeDestinatario;
    }

    public void setNomeDeDestinatario(String nomeDeDestinatario) {
        this.nomeDeDestinatario = nomeDeDestinatario;
    }

    public String getInstituicaoTransmissora() {
        return instituicaoTransmissora;
    }

    public void setInstituicaoTransmissora(String instituicaoTransmissora) {
        this.instituicaoTransmissora = instituicaoTransmissora;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public InstituicoesEnum getInstituicaoReceptora() {
        return instituicaoReceptora;
    }

    public void setInstituicaoReceptora(InstituicoesEnum instituicaoReceptora) {
        this.instituicaoReceptora = instituicaoReceptora;
    }
}
