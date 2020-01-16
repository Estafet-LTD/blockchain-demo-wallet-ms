package com.estafet.blockchain.demo.wallet.ms.jms;

import com.estafet.blockchain.demo.wallet.ms.event.MessageEventHandler;
import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.service.WalletService;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NewAccountConsumer {

    public final static String TOPIC = "new.account.topic";

    @Autowired
    private Tracer tracer;

    @Autowired
    private WalletService walletService;

    @Autowired
    private MessageEventHandler messageEventHandler;

    @Transactional
    @JmsListener(destination = TOPIC, containerFactory = "myFactory")
    public void onMessage(String message, @Header("message.event.interaction.reference") String reference) {
        try {
            if (messageEventHandler.isValid(TOPIC, reference)) {
                walletService.createWallet(Account.fromJSON(message));
            }
        } finally {
            if (tracer.activeSpan() != null) {
                tracer.activeSpan().close();
            }
        }
    }
}
