# transfer-service

Tech Stack
 - Java 8, Google Guice, SparkJava, JPA/Hibernate, H2 database, Gradle, Junit, Mockito and Cucumber

Import and Run
 - The project is gradle based, it is easily imported to any IDE as usual way
 - gradle clean build
 - java -jar build/libs/transfer-service-1.0-SNAPSHOT.jar
 - during the application start-up, it creates a schema in the H2 in-memory db.

Endpoints
  - `\transfers` to move money one account to another, sample request;

  ```
  curl -X POST \
    http://localhost:4567/transfers \
    -H 'Content-Type: application/json' \
    -d '{
      "from": {
          "sortCode": "080054",
          "number": "70328726"
      },
      "to": {
          "sortCode": "800551",
          "number": "00813797"
      },
      "amount": 145.99,
      "note": "Borrow"
  }'
  ```
  - `\transfers\{id}` to retrieve a specific transfer details
  ```
  curl -X GET http://localhost:4567/transfers/1
  ```

- Provisioned accounts details, these data is populated during application start-up.

| SORT CODE |	ACCOUNT NUMBER  |	BALANCE   |
| --- | --- | ---
| 080054        |       70328726	|    999.01    |
| 800551	    |       00813797	|    1899.12   |
| 200052	    |       75849855	|    119.09     |
| 185008	    |       12098709	|    569.33     |
| 400515	    |       20400952	|    1789.21    |

- To add more accounts to initial, add insert statement to `src/main/resources/scripts/data.sql` and restart the application.
