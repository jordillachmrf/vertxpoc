package com.marfeel.miro.service.decorator;

import org.jsoup.nodes.Document;

public class DoNothingDecorator implements MiroDecorator {

    @Override
    public Document decorate(Document document) {
        return document;
    }
}
