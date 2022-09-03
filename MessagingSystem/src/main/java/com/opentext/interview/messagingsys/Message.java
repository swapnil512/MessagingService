package com.opentext.interview.messagingsys;

import java.io.Serializable;

/**
 * POJO of Messages object
 * 
 * @author swapnil
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageId;
	private long processingTime;
	private String payload;

	public Message(String messageId, long processingTime, String payload) {
		super();
		this.messageId = messageId;
		this.processingTime = processingTime;
		this.payload = payload;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return this.messageId + "|" + this.processingTime + "|" + this.payload;
	}

}
