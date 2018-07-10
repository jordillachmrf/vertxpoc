package com.marfeel.miro.handler;

import io.vertx.core.MultiMap;

import java.net.URL;
import java.util.Objects;

public class MiroRequest {

    private final URL url;
    private final MultiMap headers;

    public MiroRequest(java.net.URL url, MultiMap headers) {
        this.url = Objects.requireNonNull(url, "Url can not be null");
        this.headers =  Objects.requireNonNull(headers, "Incoming requests headers can not be null");
    }

    public URL getURL() {
        return url;
    }

    public MultiMap getHeaders() {
        return headers;
    }
}
