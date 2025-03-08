package com.jn.swiftcodes;

import com.jn.swiftcodes.service.CsvParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RequiredArgsConstructor
public class SwiftCodesApplication implements CommandLineRunner {

	@Value("${swift-codes.csv.filepath}")
	private String FILE_PATH;
	private final CsvParserService csvParserService;
	private static final Logger logger = Logger.getLogger(SwiftCodesApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(SwiftCodesApplication.class, args);
	}

	@Override
	public void run(String... args){

		File file = new File(FILE_PATH);
		if (file.exists() && file.isFile()) {
			logger.log(Level.FINE, "Parsing CSV file ...");
			try{
				csvParserService.saveDataFromCsvToDatabase(file);
			}catch(Exception e){
				logger.log(Level.SEVERE, e.getMessage());
			}
			logger.log(Level.FINE, "Successfully saved CSV data to database");
		} else {
			logger.log(Level.SEVERE, "File does not exists " + FILE_PATH);
		}
	}
}
