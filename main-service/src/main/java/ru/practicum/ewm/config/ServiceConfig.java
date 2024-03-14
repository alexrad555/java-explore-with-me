package ru.practicum.ewm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.client.StatClient;

@Configuration
public class ServiceConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StatClient statClient() {
        return new StatClient(restTemplate());
    }
}
