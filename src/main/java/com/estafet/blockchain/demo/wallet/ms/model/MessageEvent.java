package com.estafet.blockchain.demo.wallet.ms.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document
public class MessageEvent implements Serializable {

	@NotNull
	@Id
	private String topicId;

	@NotNull
	@Field
	private String messageReference;
	
	@Field
	private Integer version;

	public MessageEvent(){

	}

	public String getTopicId() {
		return topicId;
	}

	public String getMessageReference() {
		return messageReference;
	}

	public MessageEvent setTopicId(String topicId) {
		this.topicId = topicId;
		return this;
	}

	public MessageEvent setMessageReference(String messageReference) {
		this.messageReference = messageReference;
		return this;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
