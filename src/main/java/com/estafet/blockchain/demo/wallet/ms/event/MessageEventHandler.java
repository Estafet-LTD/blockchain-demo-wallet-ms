package com.estafet.blockchain.demo.wallet.ms.event;

import com.couchbase.client.java.error.CASMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.wallet.ms.model.MessageEvent;

@Component
public class MessageEventHandler {

	@Autowired
	private MessageEventDAO messageEventDAO;

	@Transactional
	public boolean isValid(String topicName, String reference) {
		MessageEvent abstractMessageEvent = messageEventDAO.getMessageEvent(topicName);
		if (abstractMessageEvent == null || !abstractMessageEvent.getMessageReference().equals(reference)) {
			try {
				if (abstractMessageEvent == null) {
					messageEventDAO.create(new MessageEvent().setTopicId(topicName).setMessageReference(reference));
				} else {
					messageEventDAO.update(abstractMessageEvent);
				}
			} catch (CASMismatchException e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
