package com.opentext.interview.messagingsys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opentext.interview.messagingsys.utils.ProcessInfoUtils;

/**
 * This is a Task which implements Callable to process the message.<br>
 * Processing the messages means pausing for the given time.<br>
 * We are using Callable here instead of Runnable because we want to return the
 * thread, so as to check it in the calling service class.
 * 
 * @author swapnil
 *
 */
public class ProcessMessage implements Callable<Thread> {

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
	public Thread call() throws InterruptedException {
		LocalDateTime start = LocalDateTime.now();

		Thread.sleep(message.getProcessingTime());

		LocalDateTime end = LocalDateTime.now();
		String logMsg = "PID: " + ProcessInfoUtils.showProcessInfo() + ";\t" + message + ";\t\t" + "Thread:"
				+ Thread.currentThread().getId() + ";\t" + "Start:" + start.format(DateTimeFormatter.ISO_LOCAL_TIME)
				+ ";\t" + "End:" + end.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "Wait Time(ms):"
				+ ChronoUnit.MILLIS.between(start, end);
		logger.info(logMsg);
		return Thread.currentThread();
	}
}
