package com.doranco.project.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.doranco.project.config.ApplicationConfig.getClientUrl;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String clientUrl = getClientUrl();

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings for the application.
     * This allows the frontend (running on http://localhost:5173) to interact with the backend.
     *
     * @param registry The CORS registry to configure allowed origins, methods, and headers.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(clientUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Authorization","Content-Type")
                .allowCredentials(true);
    }
}

