package com.marfeel.miro.service.analyzer;

import com.marfeel.miro.service.decorator.MiroDecorator;

import java.util.List;
import java.util.Objects;

public abstract class AbstractAnalyzer<A extends MiroDecorator> implements MiroAnalyzer<A> {

    protected final List<A> decorators;

    protected AbstractAnalyzer(List<A> decorators) {
        this.decorators = Objects.requireNonNull(decorators, "Decorators list can not be null");
    }

    @Override
    public List<A> getDecorators() {
        return this.decorators;
    }
}
