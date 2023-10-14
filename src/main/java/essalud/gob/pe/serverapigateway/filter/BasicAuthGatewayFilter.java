package essalud.gob.pe.serverapigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class BasicAuthGatewayFilter implements GatewayFilter, Ordered {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private int order;

    private String code;
    private String encodedAuth;

    public BasicAuthGatewayFilter(String code, String encodedAuth) {
        this.code = code;
        this.encodedAuth = encodedAuth;
        this.order = SecurityProperties.DEFAULT_FILTER_ORDER + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String expectedBasicAuthHeader = String.format("Basic %s", encodedAuth);
        String clientBasicAuthHeader = exchange.getRequest().getHeaders().getFirst(HEADER_AUTHORIZATION);

        StringBuilder sb = new StringBuilder("[BasicAuth]: ")
                .append(String.format("(code)=%s | ", code))
                .append(String.format("(url)=%s | ", exchange.getRequest().getURI().getRawPath()))
                .append(clientBasicAuthHeader);

        if (expectedBasicAuthHeader.equals(clientBasicAuthHeader)) {
            sb.append(" | Access Granted");
            log.info(sb.toString());

            return chain.filter(exchange);
        } else {
            sb.append(" | Access Denied");
            log.info(sb.toString());

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}
