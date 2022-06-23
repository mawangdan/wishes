#!/bin/bash

nohup java -jar ./gateway/target/gateway-1.0-SNAPSHOT.jar >> ./gateway/logs/start.out &
nohup java -jar ./task/target/task-1.0-SNAPSHOT.jar >> ./task/logs/start.out &