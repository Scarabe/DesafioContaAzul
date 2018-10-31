package api.desafio.contaazul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gscarabelo
 * @since 10/30/18 10:50 AM
 */
@SpringBootApplication
@EnableAutoConfiguration
public class ApiStarter {

    public static void main (String[] args) {
        SpringApplication.run(ApiStarter.class);
    }

}
