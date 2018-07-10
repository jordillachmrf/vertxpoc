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

import io.vertx.core.Vertx;

public class JsoupSmallFsHandler extends JsoupBigFsHandler {

    public JsoupSmallFsHandler(Vertx vertx) {
        super(vertx, "hardcoded2.html");
    }

}
