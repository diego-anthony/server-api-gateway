package essalud.gob.pe.serverapigateway.util;

import essalud.gob.pe.serverapigateway.dto.RouteDto;

public abstract class RouteUtilities {

    public static String GetRouteLogInfo(RouteDto routeDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Route]: ");
        sb.append(routeDto.getPath());

        sb.append(" | [Uri]: ");
        sb.append(routeDto.getUri());

        if (!StringUtilities.isNullOrEmpty(routeDto.getRouteGroupPath())) {
            sb.append("| [Group]: ");
            sb.append(routeDto.getRouteGroupPath());
        }

        return sb.toString();
    }

    public static String GetName(String name, Integer index) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("/");
        sb.append(index);
        return sb.toString();
    }

    public static String GetPath(String path, String groupPath) {
        StringBuilder sb = new StringBuilder();

        if (!StringUtilities.isNullOrEmpty(groupPath)) {
            sb.append(groupPath);
        }

        sb.append(path);
        return sb.toString();
    }

}
