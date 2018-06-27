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

import com.marfeel.miro.handler.MiroHandler;
import com.marfeel.miro.service.MiroService;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

import java.net.URL;
import java.util.Objects;

public class DefaultHandler implements MiroHandler {

    private final MiroService miroService;

    public DefaultHandler(MiroService miroService) {
        this.miroService = Objects.requireNonNull(miroService, "MiroService can not be null");
    }

    @Override
    public Future<String> processRequest(RoutingContext context, URL url) {
        return miroService.readAndDecorateUrl(url);
    }

}
