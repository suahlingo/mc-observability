package mcmp.mc.observability.mco11yagent.monitoring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${project.version:}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Documentation")
                .version(version)
                .description("API Documentation for MC Observability Agent")
                .contact(new Contact().name("MC Team").email("mc@example.com"))
            );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("mc-observability")
            .packagesToScan("mcmp.mc.observability.mco11yagent")
            .pathsToMatch("/**")
            .build();
    }
}
