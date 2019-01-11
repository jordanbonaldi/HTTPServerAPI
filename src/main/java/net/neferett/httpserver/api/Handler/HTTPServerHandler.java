package net.neferett.httpserver.api.Handler;

import com.sun.istack.internal.Nullable;
import com.sun.net.httpserver.HttpServer;
import lombok.Data;
import lombok.SneakyThrows;
import net.neferett.httpserver.api.Processors.TaskProcessors;
import net.neferett.httpserver.api.Routing.RoutingProperties;

import java.net.InetSocketAddress;
import java.util.List;

@Data
public class HTTPServerHandler implements HttpServerInterface {

    private final int port;

    private final int threads;

    private @Nullable final String certificatePassword;

    private @Nullable final String filename;

    private HttpServer server;

    private TaskProcessors processors;

    @SneakyThrows
    public void build() {
        System.out.println("Creating new Server On Port : " + this.port);

        this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
        this.processors = new TaskProcessors(this.threads);
    }

    private void addRoute(RoutingProperties properties) {
        System.out.println("Creating new route : " + properties.getName());
        this.server.createContext(properties.getName(), properties);
    }

    public void addRoutes(List<RoutingProperties> propertiesList) {
        propertiesList.forEach(this::addRoute);
    }

    public void start() {
        this.server.setExecutor(this.processors.getExecutorService());
        System.out.println("Server started successfully");
        this.server.start();
    }

    public void stop() {
        this.server.stop(1);
        this.processors.shutDown();
    }

}
