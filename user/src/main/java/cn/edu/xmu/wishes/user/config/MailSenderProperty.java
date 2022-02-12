package cn.edu.xmu.wishes.user.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/*配置发送邮件的属性*/
@ConfigurationProperties(prefix = "user.mail")
@Component
@Data
public class MailSenderProperty {

    private String from;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String text;
    private String format;
}
