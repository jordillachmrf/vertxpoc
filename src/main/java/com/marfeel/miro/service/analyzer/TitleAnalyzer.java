package com.marfeel.miro.service.analyzer;

import com.marfeel.miro.service.decorator.TitleDecorator;
import org.jsoup.nodes.Document;

import java.util.List;

public class TitleAnalyzer extends AbstractAnalyzer<TitleDecorator> {

    public TitleAnalyzer(List<TitleDecorator> decorators) {
        super(decorators);
    }

    @Override
    public boolean applyDecorators(Document document) {
        return true;
    }
}
