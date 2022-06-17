package org.wrh.cloud.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.wrh.cloud.common.constants.Constant;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;

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
        String wanted = ctx.getRequest().getParameter("wanted");
        log.info("[1]>>> filter name: {}",name);
        if (name.equals("wrh")) {
            ctx.setSendZuulResponse(false);
            ctx.set("sendForwardFilter.ran", true);
            ctx.set("isSuccess", false);
            ctx.setResponseBody(JSON.toJSONString(ReturnResult.fail(ResultEnum.FILTER_ERROR)));
//            ctx.getResponse().setContentType("text/plain;charset=utf-8");
            ctx.getResponse().setContentType("application/json;charset=utf-8");
            if (StringUtils.isNotBlank(wanted) && wanted.equals(Constant.WANTED)) {
                log.info("[1]>>> 1/0 occured");
                int i = 1 / 0;
            }
            return null;
        }
        return null;
    }
}
