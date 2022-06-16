package org.wrh.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wrh.cloud.filter.Param2Filter;
import org.wrh.cloud.filter.ParamFilter;

/**
 * @author wuruohong
 * @date 2022-06-17 2:43
 */
@Configuration
public class FilterConfig {

    @Bean
    public ParamFilter paramFilter() {
        return new ParamFilter();
    }

    @Bean
    public Param2Filter param2Filter() {
        return new Param2Filter();
    }
}
