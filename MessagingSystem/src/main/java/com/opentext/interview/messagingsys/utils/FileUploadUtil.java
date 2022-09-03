package com.opentext.interview.messagingsys.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get("Files-Upload");

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String fileCode = "MsgFile" + LocalDate.now();

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
			System.out.println("Absolute File Path :: " + filePath.toAbsolutePath());
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Could not save file: " + fileName, ioe);
		}

		return fileCode;
	}
}
