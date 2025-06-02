# Simple Caching Server in Java

A lightweight Redis-like key-value store implemented in Java. Originally designed as a single-client in-memory store, this project has evolved to support multiple concurrent clients for single store, basic Redis commands, and RDB-style persistence.

## Features Implemented

- Basic key-value storage with `SET` and `GET` commands  
- Additional commands: `DEL`, `EXPIRE`, `TTL`, `CLOSE`, `SAVE` and `CLEAR`  
- Multi-user support: handle multiple client connections concurrently  
- In-memory data storage with expiration management  
- RDB-style snapshot persistence: save and restore data on disk  

## Technologies Used

- Java 21+ (openjdk-24) 
- Core Java libraries (no external dependencies)  
- Java concurrency utilities for multi-threaded client handling  
- File I/O for snapshot persistence  

## How to Run

1. Clone the repository and compile on your machin.
2. Go to Caching_Server\out\artifacts\Caching_Server_jar and run the jar file.
