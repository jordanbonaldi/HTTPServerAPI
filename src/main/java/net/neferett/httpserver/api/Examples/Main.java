package net.neferett.httpserver.api.Examples;

import net.neferett.httpserver.api.HTTPServerAPI;

public class Main {

    public static void main(String [] args) {

        HTTPServerAPI api = new HTTPServerAPI(8282, 10);

        api.addRoute(TestRoute.class);

        api.start();
    }

}
