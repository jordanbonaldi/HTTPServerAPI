package net.neferett.httpserver.api.Routing;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import lombok.Data;
import lombok.SneakyThrows;
import net.neferett.httpserver.api.Handler.HTTPSServerHandler;
import net.neferett.httpserver.api.Types.HttpTypes;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public abstract class RoutingProperties implements HttpHandler {

    private String name;

    private HttpTypes type;

    private String[] setParams;

    protected Map<String, String> params;

    @SneakyThrows
    public void handle(HttpExchange _t) {
        if (type.getType().equals(HTTPSServerHandler.class))
            this.https((HttpsExchange)_t);
        else
            this.build(_t);
    }

    @SneakyThrows
    private void build(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if (this.setParams.length > 0) {
            String[] tab = exchange.getRequestURI().getPath().substring(this.name.length() + 1).split("/");

            this.params = IntStream.range(0, this.setParams.length)
                    .boxed()
                    .collect(Collectors.toMap(x -> this.setParams[x], y -> tab[y]));
        }

        String response = this.routeAction(exchange).toString();

        exchange.sendResponseHeaders(200, 0);

        BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody());
        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());

        {
            byte[] buffer = new byte[2048];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        }
        out.close();
    }

    @SneakyThrows
    private void https(HttpsExchange _t) {
        _t.getSSLSession();

        this.build(_t);
    }

    public abstract JSONObject routeAction(HttpExchange t);
}
