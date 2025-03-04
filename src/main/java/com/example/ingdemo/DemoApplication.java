package com.example.ingdemo;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
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

	@Value("classpath:wikipedia-hurricane-milton-page.pdf")
	Resource hurricaneDocs;

	@Value("classpath:conference-agenda.md")
	Resource conferanceAgenda;

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder, WeatherTools myTools) {

		return args -> { // @formatter:off

			ChatClient chatClient = chatClientBuilder.build();

			//System.out.println(chatClient.prompt("Tell me a joke?").call().content());

			// SYSTEM INSTRUCTIONS & PROMPT STUFFING
			// var output = chatClient.prompt()
			// 	.system("You are a useful assistant. Be polite, and always finish the sentence with 'May the Force be with you.'")
			// 	.user(u -> u.text("Please summarize the following topic. List the afternoon talks grouped by stage :\n {topic}")
			// 			.param("topic", asText(conferanceAgenda)))
			// 	.call()
			// 	.content();
			// System.out.println(output);

			// RAG			
			// vectorStore.add(new TokenTextSplitter().split(new PagePdfDocumentReader(hurricaneDocs).read()));
			// var output = chatClient.prompt()
			// 	.system("You are useful assistant, expert in hurricanes.") // Set the system prompt
			// 	.advisors(new QuestionAnswerAdvisor(vectorStore)) // Enable RAG
			// 	.user("Was Florida hit by the Hurricane Milton?")
			// 	.call()
			// 	.content();			
			// System.out.println(output);

			// STRUCTURED OUTPUT
			// record Stage(String name, List<Talk> talks) {
			// 	record Talk(String title, String time, List<Speaker> speakers, String tags, String stage) {
			// 		record Speaker(String name, String role) {}
			// 	}		
			// }
			// List<Stage> talks = chatClient.prompt()
			// 	.system("You are a useful assistant.")
			// 	.user(u -> u.text("Get the list of talks grouped by stage. Return only talks with two or more speakers :\n {topic}")
			// 			.param("topic", asText(conferanceAgenda)))
			// 	.call()
			// 	.entity(new ParameterizedTypeReference<List<Stage>>() {});
			// System.out.println(talks);
			// System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(talks));
			
			// CHAT MEMORY
			// var chatClient2 = chatClientBuilder
			// 	.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // Enable chat memory
			// 	.build();			
			// System.out.println("Introducing the name: " + chatClient2.prompt("My name is Christian Tzolov").call().content());
			// System.out.println("Asking for the name: " + chatClient2.prompt("What is my name?").call().content());
			
			// TOOLs
			// var output = chatClient.prompt()
			// 	.tools(myTools)
			// 	.user("What to wear today in Amsterdam and in Barcelona?")
			// 	.call()
			// 	.content();				
			// System.out.println("\n" + output);
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
