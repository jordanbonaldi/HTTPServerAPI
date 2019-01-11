package net.neferett.httpserver.api.Handler;

import net.neferett.httpserver.api.Routing.RoutingProperties;

import java.util.List;

public interface HttpServerInterface {

    void build();

    void addRoutes(List<RoutingProperties> propertiesList);

    void start();

    void stop();
}
