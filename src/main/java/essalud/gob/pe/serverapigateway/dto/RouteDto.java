package essalud.gob.pe.serverapigateway.dto;

import lombok.Data;

@Data
public class RouteDto {
    private String name;
    private String path;
    private String rewritePath;
    private String uri;
    private String methods;
    private Integer securityFilterType;
    private String securityFilterId;
    private String routeGroupFilterId;
    private String routeGroupPath;
}
