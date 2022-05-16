package snowflake

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame,SaveMode,SparkSession}
import java.util.UUID

object StreamHandler {
    def main(args: Array[String]): Unit = {
        println("Hi there Tito!!")

        val spark = SparkSession.builder
        .master("local[*]")
        .appName("StreamHandler")
        .getOrCreate()

        import spark.implicits._

        // subscribe to the topic  worldcup 2022
        val inputStreamDF = spark
            .readStream
			.format("kafka") // org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.5
			.option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", "worldcup2022")
            .option("startingOffsets", "earliest")
            .load()

        inputStreamDF.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").as[(String, String)]

        // establish connection to sink db (snowflake)
        var sfOptions = Map(
            "sfURL" -> "https://gjnedoz-dmb37080.snowflakecomputing.com",
            "sfAccount" -> "dmb37080",
            "sfUser" -> "<username>",
            "sfPassword" -> "<password>",
            "sfDatabase" -> "TWITTER",
            "sfSchema" -> "PUBLIC",
            "sfRole" -> "ACCOUNTADMIN"
        )

        // write data to table
        inputStreamDF.write
            .format("snowflake")
            .options(sfOptions) 
            .option("dbtable", "TWITTER")
            .mode(SaveMode.Overwrite)
            .save()
    

    }

}