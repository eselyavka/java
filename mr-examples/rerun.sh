#!/usr/bin/env bash
hdfs dfs -rm -R -skipTrash /user/eseliavka/bfs_out* coverage
mvn -DskipTests clean package
hadoop jar target/mr-examples.jar com.example.devops.BFSDriver bfs
