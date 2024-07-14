package br.com.dsena7.pix.model.mapper;

import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.model.dto.PixResponseDTO;
import br.com.dsena7.pix.model.entity.TransacaoConsumerEntity;
import br.com.dsena7.pix.model.entity.TransacaoEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransacoesMapper {

    public static TransacaoEntity toTransacaoEntity(PixRequesDTO pixRequesDTO) {
        return TransacaoEntity.builder()
                .idDaTransacao(UUID.randomUUID().toString())
                .valor(pixRequesDTO.getValor())
                .chavePix(pixRequesDTO.getChavePix())
                .instituicaoReceptora(pixRequesDTO.getInstituicaoDeDestino())
                .nomeDeDestinatario(pixRequesDTO.getNomeDeDestinatario())
                .build();
    }

    public static PixResponseDTO toPixResponseDTO(TransacaoEntity transacaoEntity){
        return PixResponseDTO.builder()
                .idDaTransacao(transacaoEntity.getIdDaTransacao())
                .valor(transacaoEntity.getValor())
                .chavePix(transacaoEntity.getChavePix())
                .instituicaoDeDestino(transacaoEntity.getInstituicaoReceptora())
                .cpfCliente(transacaoEntity.getCpfCliente())
                .instituicao(transacaoEntity.getInstituicao())
                .nomeDeDestinatario(transacaoEntity.getNomeDeDestinatario())
                .build();
    }

    public static List<PixResponseDTO> toPixResponseDTOs(List<TransacaoEntity> transacaoEntities) {
        return transacaoEntities.stream()
                .map(TransacoesMapper::toPixResponseDTO)
                .collect(Collectors.toList());
    }
    public static TransacaoConsumerEntity toTransacaoConsumerEntity(PixRequesDTO pixRequesDTO) {
        return TransacaoConsumerEntity.builder()
                .idDaTransacao(UUID.randomUUID().toString())
                .valor(pixRequesDTO.getValor())
                .chavePix(pixRequesDTO.getChavePix())
                .instituicaoReceptora(pixRequesDTO.getInstituicaoDeDestino())
                .nomeDeDestinatario(pixRequesDTO.getNomeDeDestinatario())
                .build();
    }
}
