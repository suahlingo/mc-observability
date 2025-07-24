package mcmp.mc.observability.tracing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.*;
import org.springframework.core.Ordered;

@Configuration
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration")
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnProperty(name = "otel.enabled", havingValue = "true", matchIfMissing = true)
public class TraceConfig {

  @Value("${spring.application.name:default-service}")
  private String serviceName;

  @Value("${otel.exporter.otlp.endpoint:http://localhost:4317}")
  private String otlpEndpoint;

  @Bean
  public ExecutorService contextAwareExecutorService() {
    ExecutorService delegate = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    return new ContextAwareExecutorService(delegate);
  }

}
