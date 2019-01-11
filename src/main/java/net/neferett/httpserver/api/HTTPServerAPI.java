package net.neferett.httpserver.api;

import lombok.Data;
import lombok.SneakyThrows;
import net.neferett.httpserver.api.Handler.HttpServerInterface;
import net.neferett.httpserver.api.Routing.RouterManager;
import net.neferett.httpserver.api.Routing.RoutingProperties;
import net.neferett.httpserver.api.Types.HttpTypes;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

@Data
public class HTTPServerAPI {

    private final int port;
    private final int threads;

    private final HttpTypes type;

    private final String certificatePassword;
    private final String certificateFile;

    private HttpServerInterface server;

    private RouterManager manager;

    /**
     * Constructor for Http Server only
     * @param port server port
     * @param threads number of threads
     */
    public HTTPServerAPI(int port, int threads) {
        this(port, threads, HttpTypes.HTTP, "", "");
    }

    /**
     * Constructor for Https Server Only
     *
     * @param port server port
     * @param threads number of threads
     * @param type Define http type mode
     * @param certificatePassword Passphrase used to ssl certification
     * @param certificateFile Path of the file.pem
     */
    public HTTPServerAPI(int port, int threads, HttpTypes type, String certificatePassword, String certificateFile) {
        this.port = port;
        this.threads = threads;
        this.type = type;
        this.certificatePassword = certificatePassword;
        this.certificateFile = certificateFile;
        this.manager = new RouterManager(this.type);
        this.initServer();
    }

    @SneakyThrows
    private void initServer() {
        Constructor constructor = this.type.getType().getConstructors()[0];

        this.server = (HttpServerInterface) constructor.newInstance(this.port, this.threads, this.certificatePassword, this.certificateFile);
    }

    /**
     *
     * Add routes by package path and complete it with each name
     *
     * path + (names -> a)
     *
     * @param path package
     * @param names classes names
     * @return
     */
    public HTTPServerAPI addAllRoutesInPath(String path, String... names) {
        this.manager.addFromPathList(path, Arrays.asList(names));

        return this;
    }

    /**
     *
     * Add routes by List of classes
     *
     * @param clazz
     * @return
     */
    public HTTPServerAPI addRoutes(List<Class<? extends RoutingProperties>> clazz) {
        this.manager.addFromList(clazz);

        return this;
    }

    /**
     *
     * Add Route by class
     *
     * @param clazz
     * @return
     */
    public HTTPServerAPI addRoute(Class<? extends RoutingProperties> clazz) {
        this.manager.add(clazz);

        return this;
    }

    /**
     * Start Server
     */
    public void start() {
        this.server.build();
        this.manager.build();
        this.server.addRoutes(this.manager.getRoutes());
        this.server.start();
    }

    /**
     * Stop Server
     */
    public void stop() {
        this.server.stop();
    }

}
