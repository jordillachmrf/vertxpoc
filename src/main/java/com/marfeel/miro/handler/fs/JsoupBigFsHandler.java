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

package com.marfeel.miro.handler.fs;

import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class JsoupBigFsHandler extends AbstractFsHandler {

    public JsoupBigFsHandler(Vertx vertx) {
        this(vertx, "hardcoded.html");
    }

    protected JsoupBigFsHandler(Vertx vertx, String filename) {
        super(vertx, filename);
    }

    @Override
    public Future<MiroResponse> processRequest(RoutingContext context, MiroRequest request) {
        return super.processRequest(context, request).compose(this::applyJsoupASaco);
    }

    Future<MiroResponse> applyJsoupASaco(MiroResponse response) {
        return response.getHtml().map((String html) -> {

            Document document = Jsoup.parse(html);
            Element head = document.select("head").first();
            Element link = new Element(Tag.valueOf("meta"), "")
                    .attr("name", "marfeel-meta")
                    .attr("content", "Powered by Marfeel");
            head.appendChild(link);

            return Future.succeededFuture(new MiroResponse(document.html(), null, 200));

        }).orElseGet(() -> Future.failedFuture("Read/stored html can not be empty"));
    }

}
