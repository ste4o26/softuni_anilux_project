package anilux.anilux_spring_mvc.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

//TODO see how @ConditionalOnProperty works
@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "scheduling.enabled", matchIfMissing = true)
public class ApplicationSchedulingConfiguration {
}
