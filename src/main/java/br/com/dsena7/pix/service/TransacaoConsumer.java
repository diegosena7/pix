package br.com.dsena7.pix.service;

import br.com.dsena7.pix.config.RabbitMQConfig;
import br.com.dsena7.pix.model.mapper.TransacoesMapper;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import br.com.dsena7.pix.repository.TransacaoConsumerRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransacaoConsumer {

    @Autowired
    private TransacaoConsumerRepository transacaoConsumerRepository;

    @RabbitListener(queues = RabbitMQConfig.TRANSACTION_QUEUE)
    public void receiveTransactionMessage(PixRequesDTO transaction) {
        transacaoConsumerRepository.save(TransacoesMapper.toTransacaoConsumerEntity(transaction));
        System.out.println("Consumer received transaction: " + transaction);
    }
}
