package org.wrh.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.IPv6Utils;
import sun.net.util.IPAddressUtil;

import java.util.List;
import java.util.Map;

/**
 * @author wuruohong
 * @date 2022-06-17 2:41
 */
@Slf4j
public class ParamFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("[1]>>> enter paramfilter ");
        RequestContext ctx = RequestContext.getCurrentContext();
        String name = ctx.getRequest().getParameter("name");
        log.info("[1]>>> filter name: {}",name);
        if (name.equals("wrh")) {
            ctx.setSendZuulResponse(false);
            ctx.set("sendForwardFilter.ran", true);
            ctx.set("isSuccess", false);
            ctx.setResponseBody(name + " is vip, please sit down");
            ctx.getResponse().setContentType("text/plain;charset=utf-8");
//            ctx.getResponse().setContentType("application/json;charset=utf-8");
            return null;
        }
        return null;
    }
}
