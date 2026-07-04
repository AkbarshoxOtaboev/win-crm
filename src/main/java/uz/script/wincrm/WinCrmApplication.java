package uz.script.wincrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WinCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinCrmApplication.class, args);
    }

}
