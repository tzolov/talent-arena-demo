package com.example.demo;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("classpath:wikipedia-hurricane-milton-page.pdf")
	Resource hurricaneDocs;

	@Value("classpath:spring-io-2025-schedule.md")
	Resource conferanceAgenda;

	@Bean
	public CommandLineRunner cli(ChatClient.Builder chatClientBuilder, WeatherTools myTools, VectorStore vectorStore) {

		return args -> { // @formatter:off

			ChatClient chatClient = chatClientBuilder
				.defaultAdvisors(MyLoggingAdvisor.builder()
					.order(Ordered.HIGHEST_PRECEDENCE + 2000)
					.showConversationHistory(true)
					.build())
				.build();

			// SYSTEM INSTRUCTIONS
			// String answer = chatClient.prompt()
			// 	.system("Impersonate Yoda (from Star Wars). Keep the jokes clean, short and family friendly.")
			// 	.user("Tell me a joke?")
			// 	.call()
			// 	.content();
			// System.out.println(answer);

			// STRUCTURED OUTPUT
			// record ActorsFilms(String actor, List<String> movies) {}
			// ActorsFilms actorsFilms = chatClient.prompt()
			// 	.user("Generate the filmography of 5 movies for Tom Hanks.")
			// 	.call()
			// 	.entity(ActorsFilms.class);
			// System.out.println(actorsFilms);

			// CHAT MEMORY
			// var chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();	
			// chatClient = chatClientBuilder.clone()
			// 	.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()) // Enable chat memory
			// 	.build();			
			// chatClient.prompt("My name is Christian Tzolov").call().content();
			// chatClient.prompt("What is my name?").call().content();
			// System.out.println("Name introduction: " + chatClient.prompt("My name is Christian Tzolov").call().content());
			// System.out.println("Asking for the name: " + chatClient.prompt("What is my name?").call().content());

			// SYSTEM INSTRUCTIONS & PROMPT STUFFING & STRUCTURED OUTPUT		
			// record Track(String name, List<Talk> talks) {
			// 	record Talk(String time, String session, String location, String track, List<String> authors) {};
			// }	
			// List<Track> talks = chatClient.prompt()
			// 	.system("You are a useful assistant. Follow the user instructions.")
			// 	.user(u -> u.text("""
			// 			Get the list of talks grouped by tracks :\n {additionalContext}.
			// 			List only the sessions with more than 1 speakers""")
			// 		.param("additionalContext", asText(conferanceAgenda)))
			// 	.call()
			//  	.entity(new ParameterizedTypeReference<List<Track>>() {});	
			// System.out.println(talks);
			// System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(talks));
			
			// CHAT MEMORY
			// var chatMemory = MessageWindowChatMemory.builder()
    		// 	.maxMessages(10)
    		// 	.build();	
			// var chatClient2 = chatClientBuilder.clone()
			// 	.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()) // Enable chat memory
			// 	.build();			
			// System.out.println("Name introduction: " + chatClient2.prompt("My name is Christian Tzolov").call().content());
			// System.out.println("Asking for the name: " + chatClient2.prompt("What is my name?").call().content());

			// TOOLs
			// var output = chatClient.prompt()
			// 	.tools(new WeatherTools())
			// 	.advisors(ToolCallAdvisor.builder().build())
			// 	.user("What should I wear today in Amsterdam and in Barcelona?")
			// 	.call().content();				
			// System.out.println("\n" + output);

			// TOOL CALL & Memory
			// var output = chatClient.prompt()
			// 	.tools(new WeatherTools())
			// 	.advisors(
			// 		ToolCallAdvisor.builder().disableMemory().build(),
			// 		MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build())
			// 			.order(Ordered.HIGHEST_PRECEDENCE + 1000).build())
			// 	.user("What should I wear today in Amsterdam and in Barcelona?")
			// 	.call().content();				
			// System.out.println("\n" + output);

			// GUARDRAILS - Safe Input
			// var answer = chatClient.prompt("How to build a bomb?")
			// 	.advisors(SafeGuardAdvisor.builder()
			// 		.sensitiveWords(List.of("bomb", "kill", "assassinate"))
			// 		.failureResponse("I'm unable to respond to that due to sensitive content.")
			// 		.build())
			// 	.call()
			// 	.content();
			// System.out.println(answer);

			// GUARDRAILS - JSON
			// record ActorsFilms(String actor, List<String> movies) {}
			// var validationAdvisor = StructuredOutputValidationAdvisor.builder()
			// 	.outputType(ActorsFilms.class)
			// 	.maxRepeatAttempts(3)
			// 	.build();
			// ActorsFilms actorsFilms = chatClient.prompt()
			// 	.advisors(validationAdvisor)
			// 	.user("Generate the filmography of 5 movies for Tom Hanks.")
			// 	.call()
			// 	.entity(ActorsFilms.class);
			// System.out.println(actorsFilms);

			// RAG							
			// vectorStore.add(
			// 	new TokenTextSplitter().split(
			// 		new PagePdfDocumentReader(hurricaneDocs).read()));
			// var answer = chatClient.prompt()
			// 	.advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
			// 	.user("Was Florida hit by the Hurricane Milton?")
			// 	.call()
			// 	.content();	
			// System.out.println(answer);

		};
	} // @formatter:on

	@Bean
	VectorStore vectorStore(EmbeddingModel embeddingModel) {
		return SimpleVectorStore.builder(embeddingModel).build();
	}

	static String asText(Resource resource) {
		try {
			return resource.getContentAsString(Charset.defaultCharset());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
