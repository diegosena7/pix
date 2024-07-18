package br.com.dsena7.pix.model.entity;

import br.com.dsena7.pix.model.enums.InstituicoesEnum;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("transacao_pix")
@Builder
public class TransacaoEntity {

    @Id
    private String id;

    @Version
    private Long version;

    @Field("id_da_transacao")
    private String idDaTransacao;

    @Positive
    @Field("valor")
    private BigDecimal valor;

    @Field("chave_pix")
    private String chavePix;

    @Field("instituicao_receptora")
    private InstituicoesEnum instituicaoReceptora;

    @Builder.Default
    @Field("cpf_cliente")
    private String cpfCliente = "teste-cpf";

    @Builder.Default
    @Field("instituicao_transmissora")
    private String instituicao = "Banco do Diego";

    @Field("nome_de_destinatario")
    private String nomeDeDestinatario;

    @Builder.Default
    @Field("data_da_transacao")
    private LocalDateTime dataDaTrasacao = LocalDateTime.now();

    @Builder.Default
    @Field("transacao_processada")
    private Boolean transacaoProcessada = Boolean.FALSE;

    // Getters e Setters
    public Boolean getTransacaoProcessada() {
        return transacaoProcessada;
    }

    public void setTransacaoProcessada(Boolean transacaoProcessada) {
        this.transacaoProcessada = transacaoProcessada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getIdDaTransacao() {
        return idDaTransacao;
    }

    public void setIdDaTransacao(String idDaTransacao) {
        this.idDaTransacao = idDaTransacao;
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

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getNomeDeDestinatario() {
        return nomeDeDestinatario;
    }

    public void setNomeDeDestinatario(String nomeDeDestinatario) {
        this.nomeDeDestinatario = nomeDeDestinatario;
    }

    public LocalDateTime getDataDaTrasacao() {
        return dataDaTrasacao;
    }

    public void setDataDaTrasacao(LocalDateTime dataDaTrasacao) {
        this.dataDaTrasacao = dataDaTrasacao;
    }
}
