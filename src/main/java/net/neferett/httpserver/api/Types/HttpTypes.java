package net.neferett.httpserver.api.Types;

import lombok.Getter;
import net.neferett.httpserver.api.Handler.HTTPServerHandler;
import net.neferett.httpserver.api.Handler.HttpServerInterface;

public enum HttpTypes {

    HTTP(HTTPServerHandler.class), HTTPS(HTTPServerHandler.class);

    @Getter
    private final Class<? extends HttpServerInterface> type;

    HttpTypes(Class<? extends HttpServerInterface> type) {
        this.type = type;
    }
}
