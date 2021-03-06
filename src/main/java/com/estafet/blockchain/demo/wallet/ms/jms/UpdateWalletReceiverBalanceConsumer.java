package com.estafet.blockchain.demo.wallet.ms.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.messages.lib.wallet.UpdateWalletBalanceMessage;
import com.estafet.blockchain.demo.wallet.ms.service.WalletService;

import io.opentracing.Tracer;

@Component
public class UpdateWalletReceiverBalanceConsumer {

    public final static String TOPIC = "update.wallet.receiver.balance.topic";

    private Tracer tracer;
    private WalletService walletService;

    @Transactional
    @JmsListener(destination = TOPIC, containerFactory = "myFactory")
    public void onMessage(String message) {
        try {
            walletService.handleUpdateWalletBalanceMessage(UpdateWalletBalanceMessage.fromJSON(message));
        } finally {
            if (tracer.activeSpan() != null) {
                tracer.activeSpan().close();
            }
        }
    }

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Autowired
    public void setWalletService(WalletService walletService) {
        this.walletService = walletService;
    }
}
