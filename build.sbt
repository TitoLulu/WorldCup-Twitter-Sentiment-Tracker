ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.snowflake"


val sparkCore = "org.apache.spark" %% "spark-sql" % "3.2.1"
//val sparkSql = "org.apache.spark" %% "spark-sql" % "2.4.5" 
val snowflakeSdk = "net.snowflake" % "snowflake-ingest-sdk" % "0.10.3"
val snowflakeJdbc = "net.snowflake" % "snowflake-jdbc" % "3.13.14"


lazy val streamhandler = (project in file("."))
  .settings(
    name := "StreamHandler",
    libraryDependencies ++= Seq(sparkCore,snowflakeSdk,snowflakeJdbc)
)
  