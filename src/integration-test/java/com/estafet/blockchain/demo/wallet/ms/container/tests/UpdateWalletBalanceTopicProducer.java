package com.estafet.blockchain.demo.wallet.ms.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class UpdateWalletBalanceTopicProducer extends TopicProducer {

    public UpdateWalletBalanceTopicProducer() {
        super("update.wallet.balance.topic");
    }

    public static void send(String message) {
        new UpdateWalletBalanceTopicProducer().sendMessage(message);
    }
}
