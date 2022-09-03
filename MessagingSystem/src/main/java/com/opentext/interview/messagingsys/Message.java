package com.opentext.interview.messagingsys;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String messageId;
	private long processingTime;
	private String message;

	public Message(String messageId, long processingTime, String message) {
		super();
		this.messageId = messageId;
		this.processingTime = processingTime;
		this.message = message;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {

		return this.messageId + "|" + this.processingTime + "|" + this.message;
	}

}
