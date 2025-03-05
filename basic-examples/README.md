# Spring AI Basic Examples

This project demonstrates various features and capabilities of Spring AI, a framework that simplifies the integration of AI capabilities into Spring applications.

## Overview

The Spring AI Basic Examples project showcases how to use Spring AI to interact with Large Language Models (LLMs) like OpenAI's GPT models. It demonstrates various features including basic prompting, system instructions, structured output parsing, chat memory, and AI tools integration.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- An OpenAI API key (or other supported AI provider keys if using alternative configurations)

## Setup

1. Clone the repository
2. Set your OpenAI API key as an environment variable:
   ```
   export OPENAI_API_KEY=your-api-key
   ```
3. Build the project:
   ```
   cd basic-examples
   ./mvnw clean install
   ```
4. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

## Features

The application demonstrates the following Spring AI features:

### 1. Basic Chat Client Usage

Simple interaction with an AI model to generate responses:

```java
ChatClient chatClient = chatClientBuilder.build();
System.out.println(chatClient.prompt("Tell me a joke?").call().content());
```

### 2. System Instructions & Structured Output

Demonstrates how to:
- Set system instructions to guide the AI's behavior
- Use prompt stuffing to provide context (conference agenda data)
- Parse structured output from the AI's response into Java objects

```java
var talks = chatClient.prompt()
    .system("You are a useful assistant. Be polite, and always finish the sentence with 'May the Force be with you.'")
    .user(u -> u.text("Get the list of talks grouped by stage. Return only talks with two or more speakers :\n {topic}")
            .param("topic", asText(conferanceAgenda)))
    .call()
    .entity(new ParameterizedTypeReference<List<Stage>>() {});
```

### 3. Chat Memory

Shows how to enable and use chat memory to maintain context across multiple interactions:

```java
var chatClient2 = chatClientBuilder
    .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // Enable chat memory
    .build();
System.out.println("Introducing the name: " + chatClient2.prompt("My name is Christian Tzolov").call().content());
System.out.println("Asking for the name: " + chatClient2.prompt("What is my name?").call().content());
```

### 4. AI Tools Integration

Demonstrates how to integrate custom tools with Spring AI:

```java
var output = chatClient.prompt()
    .tools(myTools)
    .user("What to wear today in Amsterdam and in Barcelona?")
    .call()
    .content();
```

The `WeatherTools` class shows how to create a custom tool that retrieves weather data from an external API (Open-Meteo) and makes it available to the AI model.

## Project Structure

```
basic-examples/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── DemoApplication.java  # Main application class
│   │   │               └── WeatherTools.java     # Custom AI tool implementation
│   │   └── resources/
│   │       ├── application.properties  # Application configuration
│   │       └── conference-agenda.md    # Sample data for demonstrations
└── pom.xml  # Project dependencies
```

## Configuration

The application is configured to use OpenAI by default, but it also includes commented configurations for other providers like Ollama and Anthropic Claude. You can modify the `application.properties` file to switch between these providers.

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
