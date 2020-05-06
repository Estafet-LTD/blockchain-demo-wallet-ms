package com.estafet.blockchain.demo.wallet.ms.container.tests;

import com.estafet.openshift.boost.commons.lib.jms.TopicProducer;

public class DeleteAccountProducer extends TopicProducer {

    public DeleteAccountProducer() {
        super("delete.account.topic");
    }

    public static void send(String message) {
        new DeleteAccountProducer().sendMessage(message);
    }
}
