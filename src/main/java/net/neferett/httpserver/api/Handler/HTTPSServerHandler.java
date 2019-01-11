package net.neferett.httpserver.api.Handler;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import lombok.Data;
import lombok.SneakyThrows;
import net.neferett.httpserver.api.Processors.TaskProcessors;
import net.neferett.httpserver.api.Routing.RoutingProperties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.List;

@Data
public class HTTPSServerHandler implements HttpServerInterface{

    private final int port;

    private final int threads;

    private final String certificatePassword;

    private final String filename;

    private HttpsServer server;

    private TaskProcessors processors;

    private HttpsConfigurator configurator;

    @SneakyThrows
    public void build() {
        System.out.println("Creating new Server On Port : " + this.port);

        this.server = HttpsServer.create(new InetSocketAddress(this.port), 0);
        this.processors = new TaskProcessors(threads);
        this.keyStore();
    }

    private void keyStore() {
        char[] password = this.certificatePassword.toCharArray();

        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(this.filename + ".jks");
            ks.load(fis, password);

            SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            this.configurator = new HttpsConfigurator(sslContext) {
                @Override
                public void configure(HttpsParameters httpsParameters) {
                    SSLContext sslContext = getSSLContext();
                    SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                    httpsParameters.setSSLParameters(defaultSSLParameters);
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRoute(RoutingProperties properties) {
        System.out.println("Creating new route : " + properties.getName());
        this.server.createContext(properties.getName(), properties);
    }

    public void addRoutes(List<RoutingProperties> propertiesList) {
        propertiesList.forEach(this::addRoute);
    }

    public void start() {
        this.server.setExecutor(this.processors.getExecutorService());
        this.server.setHttpsConfigurator(this.configurator);
        this.server.start();
    }

    public void stop() {
        this.server.stop(1);
        this.processors.shutDown();
    }

}
