package com.opentext.interview.messagingsys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opentext.interview.messagingsys.utils.ProcessInfoUtils;

public class ProcessMessage implements Runnable {

	Logger logger = LoggerFactory.getLogger(ProcessMessage.class);
	private Message message;

	public ProcessMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public void run() {
		LocalDateTime start = LocalDateTime.now();
		long pid = ProcessInfoUtils.showProcessInfo();
		try {
			Thread.sleep(message.getProcessingTime());
		} catch (InterruptedException e) {
			logger.error("Thread InterruptedException", e);
		}
		LocalDateTime end = LocalDateTime.now();
		String logMsg = "PID: " + pid + ";\t" + message + ";\t" + "Thread:" + Thread.currentThread().getId() + ";\t"
				+ "Start:" + start.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "End:"
				+ end.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "Wait Time(ms):"
				+ ChronoUnit.MILLIS.between(start, end);
		logger.info(logMsg);

	}
}
