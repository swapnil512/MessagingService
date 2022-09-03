package com.opentext.interview.messagingsys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opentext.interview.messagingsys.utils.ProcessInfoUtils;

@Service
public class MessageService {
	Logger logger = LoggerFactory.getLogger(MessageService.class);

	public void process(MultipartFile multipartFile, int consumer) throws IOException {
		Instant start = Instant.now();
		String logStartMsg = "PID: " + ProcessInfoUtils.showProcessInfo() + "; " + "START: " + start + "; "
				+ "Consumers: " + consumer + "; " + "File: " + multipartFile.getOriginalFilename();
		logger.info(logStartMsg);

		TaskExecutor executor = createExecutor(consumer);

		InputStream inputStream = multipartFile.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		bufferedReader.lines().forEachOrdered(line -> {
			Message msg = parseMsg(line);

			if (msg.getMessageId() == null) {
				long pid = ProcessInfoUtils.showProcessInfo();
				logger.info("MessagesID is not present");
				try {
					LocalDateTime startTimeForNullMsgId = LocalDateTime.now();
					Thread.sleep(msg.getProcessingTime());
					LocalDateTime end = LocalDateTime.now();
					String logNullMsgId = "PID: " + pid + ";\t" + msg + ";\t" + "Thread:"
							+ Thread.currentThread().getId() + ";\t" + "Start:"
							+ startTimeForNullMsgId.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "End:"
							+ end.format(DateTimeFormatter.ISO_LOCAL_TIME) + ";\t" + "Wait Time(ms):"
							+ ChronoUnit.MILLIS.between(startTimeForNullMsgId, end);
					logger.info(logNullMsgId);
				} catch (InterruptedException e) {
					logger.error("Thread InterruptedException", e);
				}
			} else {
				ProcessMessage task = new ProcessMessage(msg);
				executor.execute(task);

			}
		});
	}

	private Message parseMsg(String line) {
		List<String> elements = Arrays.asList(line.split("\\s*[|]\\s*"));
		if (elements.get(0).equals("")) {
			return new Message(null, Long.valueOf(elements.get(1)), null);
		}
		return new Message(elements.get(0), Long.valueOf(elements.get(1)), elements.get(2));
	}

	private TaskExecutor createExecutor(int consumer) {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(consumer);
		executor.setMaxPoolSize(consumer);
		executor.setQueueCapacity(20);
		executor.setThreadNamePrefix("MessageThread-");
		executor.initialize();
		return executor;
	}
}
