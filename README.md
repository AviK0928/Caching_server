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

1. Clone the repository and compile on your machine.
2. Go to:
   ```bash
    Caching_Server\out\artifacts\Caching_Server_jar
   ```
    and run the jar file.

# Future Improvements
There will be many features implemented in the near future such as:  
- Additional durability via AOF Persistence support
- Multi in-memory store support for a single server
- Add more Redis commands (INCR, LPUSH, SADD, etc.)
- Enhance concurrency with fine-grained locking or atomic operations
- Add transaction and scripting support
- Develop a client library for easier usage
