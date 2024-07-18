package br.com.dsena7.pix.repository;

import br.com.dsena7.pix.model.entity.TransacaoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends MongoRepository<TransacaoEntity, String> {
    List<TransacaoEntity> findByCpfCliente(String cpfCliente);

    TransacaoEntity findByIdDaTransacao(String idDaTransacao);
}
