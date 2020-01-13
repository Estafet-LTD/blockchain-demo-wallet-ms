package com.estafet.blockchain.demo.wallet.ms.jms;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
public class BankToWalletPaymentProducer {

    public final static String TOPIC = "bank.payment.topic";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(BankPaymentMessage bankPaymentMessage) {
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend(TOPIC, bankPaymentMessage.toJSON(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("message.event.interaction.reference", UUID.randomUUID().toString());
                return message;
            }
        });
    }

}
