package com.opentext.interview.messagingsys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opentext.interview.messagingsys.utils.ProcessInfoUtils;

@Service
public class MessageService {

	Logger logger = LoggerFactory.getLogger(MessageService.class);

	/**
	 * Messages processing service which processes the input file line by line and
	 * logs the messages
	 * 
	 * @param multipartFile
	 * @param consumer
	 * @throws IOException
	 */
	public void process(MultipartFile multipartFile, int consumer) throws IOException {

		// Log the start of the process
		Instant start = Instant.now();
		String logStartMsg = "PID: " + ProcessInfoUtils.showProcessInfo() + "; " + "START: " + start + "; "
				+ "Consumers: " + consumer + "; " + "File: " + multipartFile.getOriginalFilename();
		logger.info(logStartMsg);

		/*
		 * We are using a ExecutorService which Creates a thread pool that reuses a
		 * fixed number of threads. This threads then can execute given task, which will
		 * be our messages processing task
		 */
		ExecutorService executor = Executors.newFixedThreadPool(consumer);

		/*
		 * The Hashmap is used to keep a note of which threads are executing or are done
		 * with execution, so that if the message with same Id repeats then we can wait
		 * till the first messages with same id is done processing
		 */
		Map<String, Future<Thread>> threadCheckMap = new HashMap<>();

		/*
		 * First we will read the file using InputStreamReader then if efficiency sake
		 * we will use bufferReader that uses a default-sized input buffer to store the
		 * string characters
		 */
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));

		// Using the BufferedReader, we will loop over each messages in orderly fashion
		bufferedReader.lines().forEachOrdered(line -> {

			// First will parse the messages string to its object
			Message msg = parseMsg(line);
			try {
				if (msg.getMessageId().isBlank()) {
					processMsgWithNullMsgId(msg);
				} else {
					if (threadCheckMap.containsKey(msg.getMessageId())) {
						Future<Thread> futureThread = threadCheckMap.get(msg.getMessageId());

						// Waits if necessary for the computation to complete, and then retrieves its
						// result.
						futureThread.get();

						Future<Thread> thread = executor.submit(new ProcessMessage(msg));
						threadCheckMap.put(msg.getMessageId(), thread);
					} else {
						Future<Thread> thread = executor.submit(new ProcessMessage(msg));
						threadCheckMap.put(msg.getMessageId(), thread);
					}

				}
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Thread InterruptedException or ExecutionException", e);
			}
		});

		// Till this point it is possible that some thread of the ExecutorService are
		// still not completed there execution, so we have to wait till its done.
		for (Future<Thread> future : threadCheckMap.values())
			try {
				// Waits if necessary for the computation to complete for all the threads
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Thread InterruptedException or ExecutionException", e);
			}
		// Once all the threads are done executing, print the last line of the logs
		Instant end = Instant.now();
		String logEndMsg = "PID: " + ProcessInfoUtils.showProcessInfo() + "; " + "END: " + end + ";";
		logger.info(logEndMsg);
	}

	/**
	 * This is the case when the messages ID is null. <br>
	 * So we will wait for the given time until we can start to process the next
	 * messages
	 * 
	 * @param msg parsed Messages object
	 * @throws InterruptedException
	 */
	private void processMsgWithNullMsgId(Message msg) throws InterruptedException {

		LocalDateTime startTimeForNullMsgId = LocalDateTime.now();
		// Sleep the thread to pause the executions
		Thread.sleep(msg.getProcessingTime());

		LocalDateTime endTimeForNullMsgId = LocalDateTime.now();

		String logNullMsgId = "PID: " + ProcessInfoUtils.showProcessInfo() + ";\t" + msg + ";\t" + "Thread:"
				+ Thread.currentThread().getId() + ";\t" + "Start:"
				+ startTimeForNullMsgId.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "End:"
				+ endTimeForNullMsgId.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "Wait Time(ms):"
				+ ChronoUnit.MILLIS.between(startTimeForNullMsgId, endTimeForNullMsgId);
		logger.info(logNullMsgId);
	}

	/**
	 * Parsing the message string into Messages object
	 * 
	 * @param line
	 * @return Message object
	 */
	private Message parseMsg(String line) {
		List<String> elements = Arrays.asList(line.split("\\s*[|]\\s*"));
		if (elements.get(0).equals("")) {
			return new Message("", Long.valueOf(elements.get(1)), "");
		}
		return new Message(elements.get(0), Long.valueOf(elements.get(1)), elements.get(2));
	}
}
