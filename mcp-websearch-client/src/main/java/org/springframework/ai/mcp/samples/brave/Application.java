package org.springframework.ai.mcp.samples.brave;

import java.util.stream.Collectors;
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
			
			System.out.println("Provided Tools: \n" +  Stream.of(tools.getToolCallbacks()).map(tc -> tc.getName()).collect(Collectors.joining("\n")));

			String userQuestion = "Doesn Spring AI support MCP? Please provide references and write a summary as summary.md file in the provided tmp directory.";

			System.out.println("\n\nQuestion: \n" + userQuestion);
			
			var response = chatClientBuilder.build().prompt()
					.system("You are useful assistant and can perform web searches to answer user questions.")
					.user(userQuestion)
					.tools(tools)		
					.call()
					.content();

			System.out.println("\nResponse: \n" + response);
		};
	}
}