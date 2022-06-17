package org.wrh.cloud.config;

import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;
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

    /*@Bean
    public RibbonLoadBalancerClient ribbonLoadBalancerClient() {
        return new RibbonLoadBalancerClient(HttpC);
    }*/
}
