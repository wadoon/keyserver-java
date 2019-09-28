# keyserver-java

A simple key server implementation for the [VerifyThis Long-term Challenge](https://verifythis.github.io/).

## News 

- (2019-09-29, 683be0d81364e8f6fbbc0670cef05fb2ea5f2e28)

  I created a barebone version, which does not use any HTTP library or stack.
  The barebone version uses the Java's Socket API, parses HTTP and routes by
  itself. see `HttpServer.java`.
  
  The routing is special to this project, but the HTTP should be general.
  Multi-part request are not supported.
  
  It could be fun to find a way to verify custom parsing code. 

## Getting Started

Use the Gradle Import of your favourite IDE. On the command line you can just compile and run the tests.

Compile: `./gradlew classes` and `./gradlew testClasses` 

Test: `./gradlew test` 

Run the server `./gradlew run` and then run the integration test with `./test.sh`.

## API 

This server implements `get`, `add`, `del` with their confirm counter-parts.

* **GET** `/get/:email` 

  Finds the key for the given `:email`. If `200` is returned the key is as an JSON string in the body.
  If status indicates an error, then the body holds an description of the error.
  
* **POST** `/add/:email`
  
  Submits a key (in the HTTP body) for the given `:email` address. A token is returned.
  
* **GET** `/add!/:token`
  
  Submit the token that was returned by `/add` to activate the previous sent email-key pair.
  
* **POST** `/del/:email`
 
  Mark the given assocation between `:email` and key (given in HTTP body) for deletion. 
  A token for `/del!` is returned.
  
* **GET** `/del!/:token`

  Finally deletes the association from the email-key database. 


