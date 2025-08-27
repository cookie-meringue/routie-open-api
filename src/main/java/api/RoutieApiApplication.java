package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"api", "business"})
public class RoutieApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RoutieApiApplication.class, args);
    }
}
