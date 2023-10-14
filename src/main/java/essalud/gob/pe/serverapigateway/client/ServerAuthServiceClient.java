package essalud.gob.pe.serverapigateway.client;

import essalud.gob.pe.serverapigateway.dto.RouteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "server-auth-service-client", url = "${feign-clients.server-auth-service}")
public interface ServerAuthServiceClient {

    //@GetMapping("route")
    //List<RouteDto> getRoutes();

}
