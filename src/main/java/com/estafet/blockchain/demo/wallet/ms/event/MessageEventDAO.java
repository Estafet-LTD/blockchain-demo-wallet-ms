package com.estafet.blockchain.demo.wallet.ms.event;

import com.estafet.blockchain.demo.wallet.ms.repository.MessageEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.wallet.ms.model.MessageEvent;

@Service
public class MessageEventDAO {

	@Autowired
	private MessageEventRepository messageEventRepository;

	@Transactional(readOnly = true)
	public MessageEvent getMessageEvent(String topic) {
		return messageEventRepository.findOne(topic);
	}
	
	@Transactional
	public void create(MessageEvent abstractMessageEvent) {
		messageEventRepository.save(abstractMessageEvent);
	}
	
	@Transactional
	public void update(MessageEvent abstractMessageEvent) {
		messageEventRepository.getCouchbaseOperations().update(abstractMessageEvent);
	}

}
