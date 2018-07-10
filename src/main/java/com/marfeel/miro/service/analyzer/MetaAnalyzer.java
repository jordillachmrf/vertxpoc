package com.marfeel.miro.service.analyzer;

import com.marfeel.miro.service.decorator.MetaDecorator;
import org.jsoup.nodes.Document;

import java.util.List;

public class MetaAnalyzer extends AbstractAnalyzer<MetaDecorator> {

    public MetaAnalyzer(List<MetaDecorator> decorators) {
        super(decorators);
    }

    @Override
    public boolean applyDecorators(Document document) {
        return true;
    }
}
