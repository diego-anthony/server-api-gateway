package essalud.gob.pe.serverapigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class GatewayCorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${cors.is-specific-domain}")
    private boolean isSpecificDomain;

    @Value("${cors.max-age}")
    private Long maxAge;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(maxAge);

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        if (isSpecificDomain) {
            corsConfiguration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        } else {
            corsConfiguration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
        }

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(exchange -> {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)) {
                return corsConfigurationSource.getCorsConfiguration(exchange);
            }
            return null;
        });
    }
}