package essalud.gob.pe.serverapigateway.client.web;

import essalud.gob.pe.serverapigateway.dto.RouteDto;
import essalud.gob.pe.serverapigateway.dto.SecurityFilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ServerAuthServiceWebClient {

    private final WebClient webClient;

    @Value("${server-type}")
    private Integer serverType;

    @Autowired
    public ServerAuthServiceWebClient(WebClient.Builder webClientBuilder,
                                      @Value("${feign-clients.server-auth-service}") String serverAuthServiceUrl) {

        this.webClient = webClientBuilder.baseUrl(serverAuthServiceUrl).build();
    }

    public List<RouteDto> getRoutes() {
        var data = webClient.get()
                .uri("/route/list/" + serverType)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RouteDto>>() {})
                .block();

        return data;
    }

    public SecurityFilterDto getSecurityFilter(String securityFilterId) {
        var data = webClient.get()
                .uri(String.format("/route/security-filter/%s",securityFilterId))
                .retrieve()
                .bodyToMono(SecurityFilterDto.class)
                .block();

        return data;
    }

}
