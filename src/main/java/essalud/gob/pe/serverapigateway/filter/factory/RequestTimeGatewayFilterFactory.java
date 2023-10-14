package essalud.gob.pe.serverapigateway.filter.factory;

import essalud.gob.pe.serverapigateway.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<Config> {

    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    private static final String KEY = "withParams";


    public RequestTimeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                        if (startTime != null) {

                            StringBuilder sb = new StringBuilder("[RequestTime]: ")
                                    .append(String.format("(url)=%s | ", exchange.getRequest().getURI().getRawPath()))
                                    .append(String.format("(time)=%sms", (System.currentTimeMillis() - startTime)));

                            if (config.isWithParams()) {
                                sb.append(String.format(" | (params)=%s", exchange.getRequest().getQueryParams()));
                            }

                            log.info(sb.toString());
                        }
                    })
            );
        };
    }

}
