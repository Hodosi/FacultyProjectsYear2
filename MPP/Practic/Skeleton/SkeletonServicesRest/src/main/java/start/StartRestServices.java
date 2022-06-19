package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;

@ComponentScan("skeleton")
@SpringBootApplication
public class StartRestServices {

    public static void main(String[] args){

//        SpringApplication.run(StartRestServices.class, args);
        SpringApplication app = new SpringApplication(StartRestServices.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8088"));
        app.run(args);
    }
}
