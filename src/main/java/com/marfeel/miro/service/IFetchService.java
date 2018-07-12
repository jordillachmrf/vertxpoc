package com.marfeel.miro.service;

import com.marfeel.miro.handler.MiroRequest;
import com.marfeel.miro.handler.MiroResponse;
import io.vertx.core.Future;

public interface IFetchService {
    Future<MiroResponse> fetchUrl(MiroRequest miroRequest);
}
