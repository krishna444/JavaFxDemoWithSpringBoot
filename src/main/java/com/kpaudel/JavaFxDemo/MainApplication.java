package com.kpaudel.JavaFxDemo;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		Application.launch(MyJavaFxApplication.class, args);
	}

}
