ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.snowflake"


val sparkCore = "org.apache.spark" %% "spark-sql" % "3.2.1"
//val sparkSql = "org.apache.spark" %% "spark-sql" % "2.4.5" 
val kafka_spark_sql = "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.2.1"
val snowflakeSdk = "net.snowflake" % "snowflake-ingest-sdk" % "0.10.3"
val snowflakeJdbc = "net.snowflake" % "snowflake-jdbc" % "3.13.14"
val snowflake1 = "net.snowflake" % "spark-snowflake_2.13" % "2.10.0-spark_3.2"


lazy val streamhandler = (project in file("."))
  .settings(
    name := "StreamHandler",
    libraryDependencies ++= Seq(sparkCore,kafka_spark_sql,snowflakeSdk,snowflakeJdbc,snowflake1)
)
  