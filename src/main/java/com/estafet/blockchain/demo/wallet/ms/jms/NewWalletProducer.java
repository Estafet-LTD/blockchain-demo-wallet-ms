package com.estafet.blockchain.demo.wallet.ms.jms;

import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
public class NewWalletProducer {
    public final static String TOPIC = "new.wallet.topic";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Wallet wallet) {
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend(TOPIC, wallet.toJSON(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("message.event.interaction.reference", UUID.randomUUID().toString());
                return message;
            }
        });
    }

}
