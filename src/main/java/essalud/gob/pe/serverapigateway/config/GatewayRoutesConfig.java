package essalud.gob.pe.serverapigateway.config;

import essalud.gob.pe.serverapigateway.client.web.ServerAuthServiceWebClient;
import essalud.gob.pe.serverapigateway.common.constants.SecurityFilterType;
import essalud.gob.pe.serverapigateway.dto.RouteDto;
import essalud.gob.pe.serverapigateway.filter.BasicAuthGatewayFilter;
import essalud.gob.pe.serverapigateway.filter.JwtAuthGatewayFilter;
import essalud.gob.pe.serverapigateway.util.RouteUtilities;
import essalud.gob.pe.serverapigateway.util.StringUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GatewayRoutesConfig {

    private final RouteLocatorBuilder _builder;
    private final ServerAuthServiceWebClient _serverAuthServiceWebClient;

    @Bean
    public RouteLocator customRouteLocator() {
        RouteLocatorBuilder.Builder routesBuilder = _builder.routes();
        List<RouteDto> routes = _serverAuthServiceWebClient.getRoutes();

        if (routes != null) {
            int index = 0;
            log.info("----------------------------------------");
            log.info("------------ LOADING ROUTES ------------");
            log.info("----------------------------------------");

            for (RouteDto route : routes) {
                log.info(RouteUtilities.GetRouteLogInfo(route));

                routesBuilder.route(RouteUtilities.GetName(route.getName(), ++index), r -> {
                    return r.path(RouteUtilities.GetPath(route.getPath(),route.getRouteGroupPath()))
                        .filters(f -> {
                            if (!StringUtilities.isNullOrEmpty(route.getRewritePath())) {
                                String[] rewritePathArray = route.getRewritePath().split(",");
                                f.rewritePath(RouteUtilities.GetPath(rewritePathArray[0],route.getRouteGroupPath()), rewritePathArray[1]);
                            }

                            if (!StringUtilities.isNullOrEmpty(route.getSecurityFilterId())) {

                                //TODO se puede optimizar para que de una sola llamada antes del foreach se obtenga toda la lista y filtrar en memoria
                                var securityFilter = _serverAuthServiceWebClient.getSecurityFilter(route.getSecurityFilterId());

                                if (Objects.equals(securityFilter.getType(), SecurityFilterType.BASIC_AUTH)) {
                                    f.filter(new BasicAuthGatewayFilter(securityFilter.getCode(), securityFilter.getCredentials()));
                                } else if (Objects.equals(securityFilter.getType(), SecurityFilterType.JWT)) {
                                    f.filter(new JwtAuthGatewayFilter(securityFilter.getCode(), securityFilter.getCredentials()));
                                }
                            }

                            return f;
                        }).uri(route.getUri());
                });
            }

            log.info("----------------------------------------");
        }

        return routesBuilder.build();
    }
}