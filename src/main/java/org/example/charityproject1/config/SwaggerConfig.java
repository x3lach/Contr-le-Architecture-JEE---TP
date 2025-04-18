package org.example.charityproject1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion des Campagnes de Dons")
                        .version("1.0")
                        .description("API REST pour la gestion des campagnes de dons et des transactions")
                        .contact(new Contact()
                                .name("Charity Project Team")
                                .email("contact@charityproject.org")));
    }
}
