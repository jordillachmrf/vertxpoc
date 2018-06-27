package com.marfeel.miro.configuration;

import com.marfeel.miro.service.analyzer.DoNothingAnalyzer;
import com.marfeel.miro.service.analyzer.TitleAnalyzer;
import com.marfeel.miro.service.decorator.DoNothingDecorator;
import com.marfeel.miro.service.decorator.TitleDecorator;
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
    public TitleAnalyzer titleAnalyzer() {
        return new TitleAnalyzer(asList(new TitleDecorator()));
    }

}
