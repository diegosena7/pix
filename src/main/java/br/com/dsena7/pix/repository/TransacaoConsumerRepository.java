package br.com.dsena7.pix.repository;

import br.com.dsena7.pix.model.entity.TransacaoConsumerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoConsumerRepository extends JpaRepository<TransacaoConsumerEntity, String> {
}
