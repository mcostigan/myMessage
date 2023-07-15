# MyMessage
This is a chat application that communicates in real-time using web sockets. The purpose of the application was to explore the implementation and usages of web sockets and MongoDB.

It supports several core features:
1. Register a new user with a `username` and `password`
   1. Passwords are hashed for security
2. Create new chats with 2+ users
3. Send messages to a chat
4. React to messages

## Running the application
The easiest way to run the application is with
```
docker compose up
```
This runs two docker containers: one for the API (exposed on localhost:8080) and one for MongoDB

NOTE: the Dockerfile copies over the application's `.jar` file. If you are running for the first time or have made changes to the code, you should run
```
mvn package
```
before building the image and running the container