package essalud.gob.pe.serverapigateway.config;

import essalud.gob.pe.serverapigateway.filter.RequestTimeGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayFilterConfig {

    /*@Bean
    public RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }

    @Bean
    public BasicAuthGatewayFilterFactory basicAuthGatewayFilterFactory() {
        return new BasicAuthGatewayFilterFactory();
    }*/

    @Bean
    public RequestTimeGlobalFilter requestTimeGlobalFilter() {
        return new RequestTimeGlobalFilter();
    }

}
