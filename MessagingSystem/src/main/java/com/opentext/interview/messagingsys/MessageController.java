package com.opentext.interview.messagingsys;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "interview")
public class MessageController {

	@Autowired
	private MessageService service;

	@PostMapping("/process-file/{consumerCount}")
	public ResponseEntity<Object> createStream(@RequestParam("file") MultipartFile multipartFile,
			@PathVariable("consumerCount") final int consumerCount) throws IOException {

		System.out.println("Consumer count ::" + consumerCount);

		service.process(multipartFile, consumerCount);

//		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//		long size = multipartFile.getSize();
//		System.out.println("OriginalFileName ::  " + fileName);
//		System.out.println("OriginalFileName size::   " + size);
//
//		String filecode = FileUploadUtil.saveFile(fileName, multipartFile);
//		System.out.println(filecode);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
