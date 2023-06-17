# Project Description

Project consists of Hazelcast cluster, a client and two pipeline jobs.  

The client create SQL mappings for IMaps, loads customer data into customer-map, and sends (currently hardcoded to only 9!) 
fake orders (orderId, customerId, amount) into orders-map.  The act of putting orders into the orders map initiates a 
microservice (pipeline-one) to enrich that order with the customer information and in turn put that updated information 
into the orders-with-customers-map. The act of putting entries into this map in turn initiates a second microservice 
(pipeline-two job).

Pipeline-one job is a microservice that reads from orders-map journal and enriches the orders with information from the
customer-map and outputs this information to orders-with-customers-map.  (Uses a GenericRecord with correct order 
information but dummy customer fields, and the enrichment adds the information to these dummy fields.)  

Pipeline-two job is a microservice that reads from orders-with-customers-map journal and currently just logs some of these values to the console.

### To compile:
`mvn install`

### Start Hazelcast
To start hazelcast with map journals on two imaps, and start management centre, run:
```shell
docker compose up -d
docker compose ps
docker compose logs --follow hz
```
Look at management center, http://localhost:8080


### pipeline-one:
To compile (if not already compiled from initial mvn install):
```shell
cd pipeline-one
mvn package
```

To submit job:
```shell
cd pipeline-one
hz-cli submit --class hazelcast.microservices.pipelineone.PipelineOne target/pipeline-one-1.0-SNAPSHOT.jar
```

### pipeline-two:
To compile (if not already compiled from initial mvn install):
```shell
cd pipeline-two
mvn package
```

To submit job:
```shell
cd pipeline-one
hz-cli submit --class hazelcast.microservices.pipelinetwo.PipelineTwo target/pipeline-two-1.0-SNAPSHOT.jar
```


### Run client

```shell
java -jar client/target/client-1.0-SNAPSHOT.jar
```
The client does the following
- create SQL mappings for IMaps
- loads customer data into customer-map
- puts (currently hardcoded to 9) fake orders (orderId, customerId, amount) into orders-map  

Use SQL Browser in Management Center to see results in IMaps.  
Output is also written to the hazelcast logs which should be visible in a terminal window
if ran the docker compose logs --follow hz command earlier.