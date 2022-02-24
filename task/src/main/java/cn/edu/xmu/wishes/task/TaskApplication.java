package cn.edu.xmu.wishes.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.wishes.core", "cn.edu.xmu.wishes.task"})
@MapperScan("cn.edu.xmu.wishes.task.mapper")
@EnableDiscoveryClient
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}
