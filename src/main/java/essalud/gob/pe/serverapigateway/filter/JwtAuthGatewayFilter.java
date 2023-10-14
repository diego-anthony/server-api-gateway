package essalud.gob.pe.serverapigateway.filter;

import essalud.gob.pe.serverapigateway.util.JwtUtilities;
import essalud.gob.pe.serverapigateway.util.StringUtilities;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class JwtAuthGatewayFilter implements GatewayFilter, Ordered {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private int order;

    private String code;
    private String secretKey;

    public JwtAuthGatewayFilter(String code, String secretKey) {
        this.code = code;
        this.secretKey = secretKey;
        this.order = SecurityProperties.DEFAULT_FILTER_ORDER + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientJwtAuthHeader = exchange.getRequest().getHeaders().getFirst(HEADER_AUTHORIZATION);

        StringBuilder sb = new StringBuilder("[JwtAuth]: ")
                .append(String.format("(code)=%s | ", code))
                .append(String.format("(url)=%s | ", exchange.getRequest().getURI().getRawPath()))
                .append(StringUtilities.getStringWithMaxLength(clientJwtAuthHeader, 30));

        if (StringUtils.isNotBlank(clientJwtAuthHeader) && clientJwtAuthHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            String token = clientJwtAuthHeader.replace(BEARER_TOKEN_PREFIX, "");

            sb.append("...");

            if (JwtUtilities.isValidTokenSignature(token, secretKey)) {
                if (!JwtUtilities.isTokenExpired(token, secretKey)) {
                    sb.append(" | Access Granted");
                    log.info(sb.toString());

                    return chain.filter(exchange);
                }
                else {
                    sb.append(" | Access Denied (Token Expired)");
                    log.info(sb.toString());

                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            else {
                sb.append(" | Access Denied (Invalid Token Signature)");
                log.info(sb.toString());

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } else {
            sb.append(" | Access Denied (Invalid Token)");
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