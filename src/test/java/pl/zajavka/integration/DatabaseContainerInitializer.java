package pl.zajavka.integration;

import lombok.NonNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseContainerInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext>  {


    public static final String POSTGRES_USERNAME = "username";
    public static final String POSTGRES_PASSWORD = "password";
    public static final String POSTGRES_BEAN_NAME = "postgres";
    public static final String POSTGRESQL_CONTAINER = "postgres:15.0";

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {

        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRESQL_CONTAINER)
                .withUsername(POSTGRES_USERNAME)
                .withPassword(POSTGRES_PASSWORD);
        container.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());

        applicationContext.addApplicationListener(event -> {
            if(event instanceof ContextClosedEvent) {
            container.close();
        }
        });

        applicationContext.getBeanFactory().registerSingleton(POSTGRES_BEAN_NAME,container);

    }
}
