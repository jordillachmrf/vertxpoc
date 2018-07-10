package com.marfeel.miro.handler;

import io.vertx.core.MultiMap;

import java.util.Optional;

public class MiroResponse {

    private final String html;
    private final MultiMap headers;
    private final String redirect;
    private final int statusCode;

    public MiroResponse(String html, MultiMap headers, int statusCode) {
        this.html = html;
        this.headers = headers;
        this.redirect = (this.headers != null) ? this.headers.get("Location") : null;
        this.statusCode = statusCode;
    }

    public Optional<MultiMap> getHeaders() {
        return Optional.ofNullable(headers);
    }

    public Optional<String> getHtml() {
        return Optional.ofNullable(html);
    }

    public Optional<String> getRedirect() {
        return Optional.ofNullable(this.redirect);
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
