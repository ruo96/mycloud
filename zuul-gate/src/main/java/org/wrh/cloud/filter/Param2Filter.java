package org.wrh.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wuruohong
 * @date 2022-06-17 2:41
 */
@Slf4j
public class Param2Filter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {

        RequestContext ctx = RequestContext.getCurrentContext();
        Object isSuccess = ctx.get("isSuccess");
        return isSuccess == null ? true:Boolean.parseBoolean(isSuccess.toString());
    }


    @Override
    public Object run() throws ZuulException {
        log.info("[2]>>> enter paramfilter ");

        return null;
    }
}
