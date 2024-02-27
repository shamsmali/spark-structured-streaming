/home/shams/spark-3.5.0-bin-hadoop3/bin/spark-submit \
   --class delta.streaming.DeltaStreaming \
   --master spark://192.168.1.200:7077 \
   --deploy-mode client --conf "spark.executor.cores=2" \
   --conf "spark.driver.cores=1" \
    --num-executors 2 \
    /home/shams/code/spark-structured-streaming/target/spark-structured-streaming-assembly-1.0.0.jar
