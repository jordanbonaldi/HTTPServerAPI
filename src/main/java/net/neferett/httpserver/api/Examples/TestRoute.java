package net.neferett.httpserver.api.Examples;

import com.sun.net.httpserver.HttpExchange;
import net.neferett.httpserver.api.Routing.Route;
import net.neferett.httpserver.api.Routing.RoutingProperties;
import org.json.JSONObject;

@Route(name = "/test", params = {"user", "id", "content"})
public class TestRoute extends RoutingProperties {

    @Override
    public JSONObject routeAction(HttpExchange t) {

        JSONObject obj = new JSONObject();

        this.params.forEach(obj::put);

        return obj;
    }
}