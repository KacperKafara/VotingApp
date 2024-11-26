package pl.kafara.voting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${sejm.api.url}")
    private String sejmApiUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(sejmApiUrl)
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
