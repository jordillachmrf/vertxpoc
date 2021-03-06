package com.marfeel.miro.configuration;

import com.marfeel.miro.service.analyzer.MetaAnalyzer;
import com.marfeel.miro.service.decorator.MetaDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
public class ComponentsConfiguration {

    /*@Bean
    public DoNothingAnalyzer doNothingAnalyzer() {
        return new DoNothingAnalyzer(asList(new DoNothingDecorator()));
    }
*/
    @Bean
    public MetaAnalyzer metaAnalyzer() {
        return new MetaAnalyzer(asList(new MetaDecorator()));
    }

}
