package com.mercadolibre.bootcamp.projeto_integrador.mapper;

import com.mercadolibre.bootcamp.projeto_integrador.model.Route;
import com.mercadolibre.bootcamp.projeto_integrador.payload.RouteResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RouteMapper {

    public RouteResponse mapFromRouteToRouteResponse(Route route){

        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setFrom(route.getFrom());
        routeResponse.setDestination(route.getDestination());
        routeResponse.setDuration(route.getDuration());
        routeResponse.setId(route.getId() != null ? route.getId() : null);

        return routeResponse;
    }

    public List<RouteResponse> mapRouteListToRouteResponseList(final List<Route> routeList) {
        return routeList != null
                ? routeList
                .stream()
                .map(this::mapFromRouteToRouteResponse)
                .collect(Collectors.toList())
                : null;
    }
}
