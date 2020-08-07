package com.estafet.blockchain.demo.wallet.ms.container.tests;

import com.estafet.boostcd.jms.TopicProducer;

public class NewAccountProducer extends TopicProducer {

    public NewAccountProducer() {
        super("new.account.topic");
    }

    public static void send(String message) {
        new NewAccountProducer().sendMessage(message);
    }
}
