package com.estafet.blockchain.demo.wallet.ms.jms;

import com.estafet.blockchain.demo.messages.lib.wallet.UpdateWalletBalanceMessage;
import com.estafet.blockchain.demo.wallet.ms.service.WalletService;
import com.estafet.demo.commons.lib.event.MessageEventHandler;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateWalletBalanceConsumer {

    public final static String TOPIC = "update.wallet.balance.topic";

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
                walletService.handleUpdateWalletBalanceMessage(UpdateWalletBalanceMessage.fromJSON(message));
            }
        } finally {
            if (tracer.activeSpan() != null) {
                tracer.activeSpan().close();
            }
        }
    }
}
