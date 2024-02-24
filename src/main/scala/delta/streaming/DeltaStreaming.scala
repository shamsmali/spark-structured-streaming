package delta.streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.delta.implicits.stringStringEncoder
import org.apache.spark.sql.streaming.{Trigger}

import scala.concurrent.duration._

object DeltaStreaming {

  def main(args: Array[String]): Unit = {
    val accessKey = "admin"
    val secretKey = "password"
    val s3Url = "http://192.168.1.200:9000"

    val spark = SparkSession
      .builder()
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .appName("delta-kafka-consumer").getOrCreate()

    spark.sparkContext.hadoopConfiguration.set("spark.sql.debug.maxToStringFields", "100")
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

    spark.sparkContext.hadoopConfiguration.set("fs.s3a.access.key", accessKey)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.secret.key", secretKey)
    spark.sparkContext.hadoopConfiguration.set("fs.s3a.endpoint", s3Url)

    val personTablePath = "s3a://spark-tutorial/person-events"
    val checkPointLocation = "s3a://spark-tutorial/person-events-checkpoint"


    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "192.168.1.200:9092,192.168.1.200:9093")
      .option("subscribe", "person_events")
      .load()
    val ds = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]



    val query = ds.writeStream
      .option("path",  personTablePath)
      .outputMode("append")
      .option("truncate", false)
      .option("checkpointLocation", checkPointLocation)
      .trigger(Trigger.ProcessingTime(30.seconds))
      .start()


    query.awaitTermination()
  }
}
