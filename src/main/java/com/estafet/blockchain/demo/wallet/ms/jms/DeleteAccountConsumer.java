package com.estafet.blockchain.demo.wallet.ms.jms;

import com.estafet.blockchain.demo.wallet.ms.model.Account;
import com.estafet.blockchain.demo.wallet.ms.service.WalletService;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteAccountConsumer {

    public final static String TOPIC = "delete.account.topic";

    @Autowired
    private Tracer tracer;

    @Autowired
    private WalletService walletService;

    @Transactional
    @JmsListener(destination = TOPIC, containerFactory = "myFactory")
    public void onMessage(String message) {
        try {
            walletService.createWallet(Account.fromJSON(message));
        } finally {
            if (tracer.activeSpan() != null) {
                tracer.activeSpan().close();
            }
        }
    }
}
