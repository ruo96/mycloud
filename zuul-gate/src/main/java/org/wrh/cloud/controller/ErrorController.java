package org.wrh.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wuruohong
 * @date 2022-06-17 9:02
 * 是否不针对正常业务里面的异常呢
 *
 * 这个过滤器异常处理controller只能有一个，因此那个getErrorPath里面都是需要写死的
 */
@Slf4j
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
//        return "/common/";
        return "/error";
    }

//    @RequestMapping("/common/")
    @RequestMapping("/error")
    public ReturnResult error(HttpServletRequest request) {
        log.info("[filter-error-handler]>>> enter ");
        Map<String, Object> errorAttributes = getErrorAttributes(request);
        String message = (String) errorAttributes.get("message");
        String trace = (String) errorAttributes.get("trace");
        if (StringUtils.isNotBlank(trace)) {
            message = String.format( "[errorHandler]>>> message is : %s |||| and trace is ： %s", message, trace);
        }
        log.error("[filter-error-handler]>>> msg: {}", message);
        return ReturnResult.fail(ResultEnum.FILTER_ERROR_RESP);
//        return ResultEnum.FILTER_ERROR.getMsg();
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
        return errorAttributes.getErrorAttributes(new ServletWebRequest(request), true);
    }
}
