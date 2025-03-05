package org.springframework.ai.mcp.samples.brave;

import java.util.stream.Stream;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner chatbot(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools) {

		return args -> {
			
			System.out.println("Provided Tools: " +  Stream.of(tools.getToolCallbacks()).map(tc -> tc.getName()).toList());

			String userQuestion = "Create a summary about the Talent Arena conference";

			System.out.println("User Question: " + userQuestion);
			
			var response = chatClientBuilder.build().prompt()
					.system("You are useful assistant and can perform web searches Brave's search API to reply to your questions.")
					.user(userQuestion)
					.tools(tools)
					.call()
					.content();

			System.out.println("Response: " + response);
		};
	}
}