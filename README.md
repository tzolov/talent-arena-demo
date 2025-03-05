# Spring AI Examples

This repository contains a collection of examples demonstrating various features and capabilities of Spring AI, a framework that simplifies the integration of AI capabilities into Spring applications.

## Repository Structure

This repository is organized into several modules, each showcasing different aspects of Spring AI:

- **basic-examples**: Demonstrates core Spring AI features including basic prompting, system instructions, structured output parsing, chat memory, and AI tools integration.
- **mcp-weather-server**: A Model Context Protocol (MCP) server implementation that provides weather data using Spring WebFlux.
- **mcp-websearch-client**: An interactive chatbot that combines Spring AI's MCP with Brave Search to provide up-to-date information from the web.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- API keys for the services you want to use:
  - OpenAI API key (for basic examples)
  - Anthropic API key (for MCP websearch client)
  - Brave Search API key (for MCP websearch client)

## Basic Examples

The basic examples project showcases how to use Spring AI to interact with Large Language Models (LLMs) like OpenAI's GPT models.

### Features

- Basic Chat Client Usage
- System Instructions & Structured Output
- Chat Memory
- AI Tools Integration (Weather data)

### Setup

```bash
cd basic-examples
export OPENAI_API_KEY=your-api-key
./mvnw clean install
./mvnw spring-boot:run
```

## MCP Weather Server

This module demonstrates how to create a Spring AI Model Context Protocol (MCP) server that provides weather data using Spring WebFlux.

### Features

- Spring Boot starter for MCP server implementation
- Multiple transport modes (WebFlux SSE and STDIO)
- Weather tool that integrates with the Open-Meteo API

### Setup

```bash
cd mcp-weather-server
./mvnw clean package
./mvnw spring-boot:run
```

### Available Tools

- **Weather Tool**: Get temperature data for specific locations using the Open-Meteo API

## MCP Websearch Client

This example demonstrates how to build an interactive chatbot that combines Spring AI's Model Context Protocol (MCP) with the Brave Search MCP Server.

### Features

- Persistent chatbot that maintains conversation history
- Integration with Brave Search for real-time web data
- Powered by Anthropic's Claude AI model

### Setup

```bash
cd mcp-websearch-client
export ANTHROPIC_API_KEY='your-anthropic-api-key-here'
export BRAVE_API_KEY='your-brave-api-key-here'
./mvnw clean install
./mvnw spring-boot:run
```

## License

This project is licensed under the Apache License 2.0.
