package v.talk.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry  registry) {
                registry.addMapping("/**")
                		.allowedOriginPatterns("https://conversia.darshangolchha.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
                        .allowCredentials(true) // Allow sending cookies from the client
                        .maxAge(3600); // Set the max age of the CORS pre-flight request
            }
        };
    }
}


