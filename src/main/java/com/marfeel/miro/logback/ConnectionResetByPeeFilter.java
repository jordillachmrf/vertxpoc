package com.marfeel.miro.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ConnectionResetByPeeFilter extends Filter<ILoggingEvent> {

    private final String MESSAGE_1 = "forcibly closed by the remote";
    private final String MESSAGE_2 = "java.io.IOException: Connection reset by peer";
    private final String CLASS = "io.vertx.core.net.impl.ConnectionBase";

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event != null && event.getLoggerName().equals(CLASS) && (event.getMessage() != null && (event.getMessage().contains(MESSAGE_1) ||  event.getMessage().contains(MESSAGE_2)))) {
          return FilterReply.DENY;
        } else {
          return FilterReply.NEUTRAL;
        }
    }
}
