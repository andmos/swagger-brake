package io.redskap.swagger.brake.maven;

import io.redskap.swagger.brake.maven.http.HttpClientErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class MavenConfiguration {
    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create().addInterceptorLast(new HttpClientErrorHandler()).build();
    }
}
