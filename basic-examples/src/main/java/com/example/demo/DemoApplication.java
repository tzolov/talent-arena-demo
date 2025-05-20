package com.example.demo;

import java.nio.charset.Charset;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("classpath:gosim-ai-schedule.md")
	Resource conferanceAgenda;

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder, WeatherTools myTools) {

		return args -> { // @formatter:off

			ChatClient chatClient = chatClientBuilder.build();

			// System.out.println(chatClient.prompt("Tell me a joke?").call().content());

			// SYSTEM INSTRUCTIONS & PROMPT STUFFING & STRUCTURED OUTPUT		
			// record Track(String name, List<Talk> talks) {
			// 	record Talk(String time, String session, String location, String track) {};
			// }	
			// List<Track> talks = chatClient.prompt()
			// 	.system("You are a useful assistant. Follow the user instructions.")
			// 	.user(u -> u.text("Get the list of talks grouped by tracks :\n {additionalContext}")
			// 			.param("additionalContext", asText(conferanceAgenda)))
			// 	.call()
			//  	.entity(new ParameterizedTypeReference<List<Track>>() {});				
			// System.out.println(talks);
			// System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(talks));
			
			// CHAT MEMORY
			var chatMemory = MessageWindowChatMemory.builder()
    			.maxMessages(10)
    			.build();		
			var chatClient2 = chatClientBuilder
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()) // Enable chat memory
				.build();			
			System.out.println("Introducing the name: " + chatClient2.prompt("My name is Christian Tzolov").call().content());
			System.out.println("Asking for the name: " + chatClient2.prompt("What is my name?").call().content());
			
			// TOOLs
			var output = chatClient.prompt()
				.tools(myTools)
				.user("What should I wear today in Amsterdam and Paris?")
				.call()
				.content();				
			System.out.println("\n" + output);
		};
	} // @formatter:on

	static String asText(Resource resource) {
		try {
			return resource.getContentAsString(Charset.defaultCharset());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
