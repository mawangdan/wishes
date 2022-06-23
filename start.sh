#!/bin/bash

nohup java -jar ./gateway/target/gateway-1.0-SNAPSHOT.jar >> ./logs/gateway.out &
nohup java -jar ./task/target/task-1.0-SNAPSHOT.jar >> ./logs/task.out &