# Spark-Structured-Streaming

A Twitter STreaming application that continuously pulls tweets relating to the topic WorldCup2022/Qatar World Cup from Twitter API, processes the output into a Kafka topic that is then consumed by Spark, transformations applied and the resultant data pushed to a SnowFlake sink database.

The project builds on to my learnings on building Structured STreaming pipelines using Spark + Kafka

# How to get started with the project 

1. Install SBT as per the guidelines provide [here](https://www.scala-sbt.org/1.x/docs/Setup.html)
2. Clone this repo into your workspace 
3. Add your Twitter Developer account credentials to the file twitter.cfg 
4. Add your SnowFlake User credentials to the StreamHandler.scala file 
5. Start Kafka with the command sudo docker-compose up -d 
6. In your editor run the command sbt to access the sbt shell
7. Inside sbt shell type compile to package the project 
8. Next type the command run 
9. Run app.py script 

Head over to your SnowFlake table and confirm that data is being added to your database
