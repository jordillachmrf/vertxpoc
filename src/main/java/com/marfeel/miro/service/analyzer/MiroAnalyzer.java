package com.marfeel.miro.service.analyzer;

import com.marfeel.miro.service.decorator.MiroDecorator;
import org.jsoup.nodes.Document;

import java.util.List;

public interface MiroAnalyzer<D extends MiroDecorator> {

    boolean applyDecorators(Document document);

    List<D> getDecorators();

}
