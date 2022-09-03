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

/**
 * Messages controller class for Calling the process messages endpoint. <br>
 * Endpoint :: * "../interview/process-file/5" <br>
 * 
 * Messages file consist of | separated multiple lines of strings<br>
 * for Example: <br>
 * A|1000|Monday <br>
 * B|1000|Wednesday <br>
 * D|3000|Friday <br>
 * A|50|Tuesday <br>
 * B|100|Thursday <br>
 * |10000| <br>
 * D|100|Saturday<br>
 * 
 * 
 * @author swapnil
 *
 */
@RestController
@RequestMapping(value = "interview")
public class MessageController {

	@Autowired
	private MessageService service;

	/**
	 * POST method which consumes the Multi-Part file containing the messages.
	 * 
	 * @param multipartFile - the file containing the messages
	 * @param consumers     - number of consumers to use to process the file.
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/process-file/{consumers}")
	public ResponseEntity<Object> processMessagesFile(@RequestParam("file") MultipartFile multipartFile,
			@PathVariable("consumers") final int consumers) throws IOException {

		service.process(multipartFile, consumers);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
