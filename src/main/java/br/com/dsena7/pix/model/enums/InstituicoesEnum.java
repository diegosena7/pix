package br.com.dsena7.pix.model.enums;

public enum InstituicoesEnum {
    BRADESCO("Bradesco"),
    ITAU("Itaú"),
    CAIXA("Caixa"),
    NUBANK("Nubank"),
    INTER("Inter");

    private String nome;

    InstituicoesEnum(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
