package essalud.gob.pe.serverapigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class RequestTimeGlobalFilter implements GlobalFilter, Ordered {

    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    private int order;

    public RequestTimeGlobalFilter() {
        this.order = SecurityProperties.DEFAULT_FILTER_ORDER + 1;
        log.info("[RequestTimeGlobalFilter]: Loaded");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                    if (startTime != null) {

                        StringBuilder sb = new StringBuilder("[RequestTime]: ")
                                .append(String.format("(url)=%s | ", exchange.getRequest().getURI().getRawPath()))
                                .append(String.format("(time)=%sms", (System.currentTimeMillis() - startTime)));

                        /*
                        if (config.isWithParams()) {
                            sb.append(String.format(" | (params)=%s", exchange.getRequest().getQueryParams()));
                        }*/

                        log.info(sb.toString());
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return order;
    }

}
