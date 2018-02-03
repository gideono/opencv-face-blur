package se.bbs.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import se.bbs.service.Preview;

@SpringBootConfiguration
public class Configuration {

    private Environment env;

    @Autowired
    public Configuration(Environment environment) {
        this.env = environment;
    }
}
