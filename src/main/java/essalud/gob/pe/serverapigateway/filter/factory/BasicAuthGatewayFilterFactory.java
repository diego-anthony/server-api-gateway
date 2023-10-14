package essalud.gob.pe.serverapigateway.filter.factory;

import essalud.gob.pe.serverapigateway.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
public class BasicAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String KEY = "withParams";

    //@Value("${security.basic.common-service.user}")
    private String user = "test";

    //@Value("${security.basic.common-service.pass}")
    private String pass = "test";

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY);
    }

    public BasicAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String id = config.getId();

            String expectedAuth = String.format("%s:%s", user, pass);
            String expectedEncodedAuth = Base64.getEncoder().encodeToString(expectedAuth.getBytes());
            String expectedBasicAuthHeader = String.format("Basic %s", expectedEncodedAuth);

            String clientBasicAuthHeader = exchange.getRequest().getHeaders().getFirst(HEADER_AUTHORIZATION);

            StringBuilder sb = new StringBuilder("[BasicAuth]: ")
                    .append(String.format("(id)=%s | ", id))
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
        };
    }

}
