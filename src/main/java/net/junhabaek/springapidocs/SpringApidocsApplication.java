package net.junhabaek.springapidocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class SpringApidocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringApidocsApplication.class, args);
    }

}
