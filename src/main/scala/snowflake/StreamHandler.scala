package snowflake

// import net.snowflake.spark.snowflake.SnowflakeConnectorUtils
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame,SaveMode,SparkSession}
import java.util.UUID

case class TwitterData(user_id: Long, created_at: String, num_followers: Long, location: String, num_favourites: Long,num_retweets: Long)

object StreamHandler {
    def main(args: Array[String]): Unit = {
        println("Hi there Tito!!")

        val spark = SparkSession.builder
        .master("local[*]")
        .appName("StreamHandler")
        .getOrCreate()

        import spark.implicits._

        // subscribe to the topic  worldcup 
        val inputStreamDF = spark
            .readStream
			.format("kafka") // org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.5
			.option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", "worldcup")
            .option("startingOffsets", "earliest")
            .load()
                
        inputStreamDF.selectExpr("CAST(value AS STRING)").as[String]
        println("We are here !!!", "=>",inputStreamDF.printSchema())


        var sfOptions = Map(
            "sfURL" -> "https://gjnedoz-dmb37080.snowflakecomputing.com",
            "sfAccount" -> "dmb37080",
            "sfUser" -> "<username>",
            "sfPassword" -> "<password>",
            "sfDatabase" -> "TWITTER",
            "sfSchema" -> "PUBLIC",
            "sfRole" -> "ACCOUNTADMIN"
        )

        // val SNOWFLAKE_SOURCE_NAME = "net.snowflake.spark.snowflake"

        // write data to table
        val query = inputStreamDF.writeStream
            .trigger(Trigger.ProcessingTime("30 seconds"))
            .foreachBatch{(batchDF:DataFrame, batchID: Long) => 
            batchDF.write
                .format("console")
                .options(sfOptions) 
                .option("dbtable", "TWITTER")
                .mode(SaveMode.Append)
                .save()
            }
            .outputMode("update")
            .start()
        
        query.awaitTermination()

    }

}