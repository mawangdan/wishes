package cn.edu.xmu.plack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan({"cn.edu.xmu.plack.task.mapper", "cn.edu.xmu.plack.user.mapper","cn.edu.xmu.plack.news.mapper"})
@EnableConfigurationProperties
public class PlackApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlackApplication.class, args);
    }
}
