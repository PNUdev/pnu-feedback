package com.pnu.dev.pnufeedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class PnuFeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PnuFeedbackApplication.class, args);
    }

}
