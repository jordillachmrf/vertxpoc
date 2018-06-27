package com.marfeel.miro.service.analyzer;

import com.marfeel.miro.service.decorator.DoNothingDecorator;
import org.jsoup.nodes.Document;

import java.util.List;

public class DoNothingAnalyzer extends AbstractAnalyzer<DoNothingDecorator> {

    public DoNothingAnalyzer(List<DoNothingDecorator> decorators) {
        super(decorators);
    }

    @Override
    public boolean applyDecorators(Document document) {
        return true;
    }
}
