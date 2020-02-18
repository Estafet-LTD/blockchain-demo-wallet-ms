package com.estafet.blockchain.demo.wallet.ms.container.tests;

import com.estafet.blockchain.demo.messages.lib.wallet.UpdateWalletBalanceMessage;
import com.estafet.openshift.boost.commons.lib.jms.TopicConsumer;

public class WalletPaymentConsumer extends TopicConsumer {

	public WalletPaymentConsumer() {
		super("update.wallet.balance.topic");
	}

	public UpdateWalletBalanceMessage consume() {
		return super.consume(UpdateWalletBalanceMessage.class);
	}
}
