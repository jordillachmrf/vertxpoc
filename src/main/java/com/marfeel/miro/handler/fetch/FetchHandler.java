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

package com.marfeel.miro.handler.fetch;

import com.marfeel.miro.handler.MiroHandler;
import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import com.marfeel.miro.service.IFetchService;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;

public class FetchHandler implements MiroHandler {

    private final IFetchService IFetchService;

    public FetchHandler(IFetchService fetchService) {
        this.IFetchService = Objects.requireNonNull(fetchService, "FetchService can not be null");
    }

    @Override
    public Future<MiroResponse> processRequest(RoutingContext context, MiroRequest request) {
        return IFetchService.fetchUrl(request);
    }

}
