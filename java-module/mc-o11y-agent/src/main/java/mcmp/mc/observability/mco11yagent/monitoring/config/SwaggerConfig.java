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
                .title("MC Observability APIs")
                .version(version)
                .description("API Documentation for all MC Observability modules")
                .contact(new Contact().name("MC Team").email("mc@example.com"))
            );
    }


    @Bean
    public GroupedOpenApi agentApi() {
        return GroupedOpenApi.builder()
            .group("mc-o11y-agent")
            .packagesToScan("mcmp.mc.observability.mco11yagent")
            .pathsToMatch("/agent/**")
            .build();
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
            .group("mc-o11y-manager")
            .packagesToScan("mcmp.mc.observability.mco11ymanager")
            .pathsToMatch("/manager/**")
            .build();
    }

    @Bean
    public GroupedOpenApi o11yManagerApi() {
        return GroupedOpenApi.builder()
            .group("o11y-manager")
            .packagesToScan("mcmp.mc.observability.o11ymanager")
            .pathsToMatch("/api/**")
            .build();
    }
}
