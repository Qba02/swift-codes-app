package com.jn.swiftcodes;

import com.jn.swiftcodes.service.CsvParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class SwiftCodesApplication implements CommandLineRunner {

	@Value("${swift-codes.csv.filepath}")
	private String FILE_PATH;
	private final CsvParserService csvParserService;
	private static final Logger logger = Logger.getLogger(SwiftCodesApplication.class.getName());

	@Autowired
	public SwiftCodesApplication(CsvParserService csvParserService){
		this.csvParserService = csvParserService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SwiftCodesApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Resource resource = new ClassPathResource(FILE_PATH);
		try (InputStream inputStream = resource.getInputStream()){
			logger.log(Level.FINE, "Parsing CSV file ...");
			csvParserService.saveDataFromCsvToDatabase(inputStream);
			logger.log(Level.FINE, "Successfully saved CSV data to database");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error processing the CSV file: " + e.getMessage());
		}
	}
}
