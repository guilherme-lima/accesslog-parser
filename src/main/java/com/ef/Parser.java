package com.ef;

import com.ef.usecase.ReadFile;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
@SpringBootApplication
@AllArgsConstructor
public class Parser implements CommandLineRunner {

    private ReadFile readFileUseCase;

	public static void main(String[] args) {
		SpringApplication.run(Parser.class, args);
	}

    @Override
    public void run(String... args) throws DateTimeParseException, IOException {
	    if (args.length >= 3)
	        readFileUseCase.execute(args);
    }
}