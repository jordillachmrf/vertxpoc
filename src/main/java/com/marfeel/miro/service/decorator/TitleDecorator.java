package com.marfeel.miro.service.decorator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class TitleDecorator implements MiroDecorator {

    @Override
    public Document decorate(Document document) {
        Element head = document.select("head").first();
        Element link = new Element(Tag.valueOf("meta"), "")
                .attr("name", "marfeel-meta")
                .attr("content", "Powered by Marfeel");
        head.appendChild(link);
        return document;
    }
}
