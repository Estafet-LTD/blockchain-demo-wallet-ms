package com.estafet.blockchain.demo.wallet.ms.jms;

import com.estafet.blockchain.demo.messages.lib.wallet.NewWalletMessage;
import com.estafet.blockchain.demo.messages.lib.wallet.WalletPaymentMessage;
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

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Wallet wallet) {
        jmsTemplate.setPubSubDomain(true);
        NewWalletMessage newWalletMessage = convertToNewWalletMess(wallet);
        jmsTemplate.convertAndSend("new.wallet.topic", newWalletMessage.toJSON(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("message.event.interaction.reference", UUID.randomUUID().toString());
                return message;
            }
        });
    }

    public NewWalletMessage convertToNewWalletMess (Wallet wallet){
        NewWalletMessage newWalletMessage = new NewWalletMessage();
        newWalletMessage.setWalletAddress(wallet.getWalletAddress());
        newWalletMessage.setWalletName(wallet.getWalletName());
        return newWalletMessage;
    }
}
