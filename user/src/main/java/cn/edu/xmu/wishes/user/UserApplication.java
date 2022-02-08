package cn.edu.xmu.wishes.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.wishes.core", "cn.edu.xmu.wishes.user"})
@EnableConfigurationProperties
@MapperScan("cn.edu.xmu.wishes.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
