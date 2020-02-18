package com.estafet.blockchain.demo.wallet.ms.container.tests;

import com.estafet.openshift.boost.commons.lib.jms.TopicProducer;

public class UpdateWalletReceiverBalanceTopicProducer extends TopicProducer {

    public UpdateWalletReceiverBalanceTopicProducer() {
        super("update.wallet.receiver.balance.topic");
    }

    public static void send(String message) {
        new UpdateWalletReceiverBalanceTopicProducer().sendMessage(message);
    }
}
