package com.weweibuy.common.util.email.config;

import com.weweibuy.common.util.email.EmailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 邮件配置
 *
 * @author durenhao
 * @date 2021/12/10 22:00
 **/
@Configuration
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class CommonEmailSendConfig {


    @Bean
    public EmailSender emailSender(JavaMailSender javaMailSender) {
        return new EmailSender(javaMailSender);
    }

}
