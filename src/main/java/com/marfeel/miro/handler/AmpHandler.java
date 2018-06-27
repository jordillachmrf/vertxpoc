/*
 * Copyright (c) 2018 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

package com.marfeel.miro.handler;

import com.marfeel.miro.service.MiroService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class AmpHandler implements Handler<RoutingContext> {

    private final MiroService miroService;
    private final String html;

    public AmpHandler(MiroService miroService) {
        this.miroService = Objects.requireNonNull(miroService, "MiroService can not be null");
        try {
            this.html = new String(Files.readAllBytes(Paths.get(this.getClass().getResource("/hardcoded.html").getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(RoutingContext routingContext) {
        // TODO add needed params or whatever to retrieve amp version
        Document document = Jsoup.parse(this.html);
        Element head = document.select("head").first();
        Element link = new Element(Tag.valueOf("meta"), "")
                .attr("name", "marfeel-meta")
                .attr("content", "Powered by Marfeel");
        head.appendChild(link);

        routingContext.response().setStatusCode(200).end(document.html() /*"{\"amp\": true}"*/);
    }
}
