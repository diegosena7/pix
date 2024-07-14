package br.com.dsena7.pix.service;

import br.com.dsena7.pix.config.RabbitMQConfig;
import br.com.dsena7.pix.model.dto.PixRequesDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransacaoProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTransactionMessage(PixRequesDTO transaction) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_QUEUE, transaction);
    }
}

