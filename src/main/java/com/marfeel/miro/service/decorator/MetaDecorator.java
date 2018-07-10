package com.marfeel.miro.service.decorator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class MetaDecorator implements MiroDecorator {

    @Override
    public Document decorate(Document document) {
        Element head = document.select("head").first();
        Element link = new Element(Tag.valueOf("meta"), "")
                .attr("name", "mrf-tech")
                .attr("content", "feo");
        head.appendChild(link);
        return document;
    }
}
