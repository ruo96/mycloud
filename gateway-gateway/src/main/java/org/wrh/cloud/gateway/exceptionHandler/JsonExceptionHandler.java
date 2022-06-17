package org.wrh.cloud.gateway.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义网关的异常处理
 *
 * 异常是用JSON代替HTML异常信息
 * @date 2021/11/29
 *
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {
        private static Logger logger = LoggerFactory.getLogger(JsonExceptionHandler.class);

        public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                    ErrorProperties errorProperties, ApplicationContext applicationContext) {
                super(errorAttributes, resourceProperties, errorProperties, applicationContext);
        }

        /**
         * 获取异常属性
        */
        @Override
        protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
            int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            Throwable error = super.getError(request);
            if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
               code = HttpStatus.NOT_FOUND.value();
            }else if(error instanceof ResponseStatusException){
                code = ((ResponseStatusException) error).getStatus().value();
            }

            return response(code, request.path(), this.buildMessage(request, error));
        }

        /**
        * 指定响应处理方法为JSON处理的方法
        * @param errorAttributes
        */
         @Override
        protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
             /** 这个是优先返回html*/
             return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
             /** 如果异常时候想返回json而不是html*/
             // return RouterFunctions.route(acceptsTextHtml(), this::renderErrorView);
         }

         /**
          * 根据code获取对应的HttpStatus
          * @param errorAttributes
         */
       @Override
       protected int getHttpStatus(Map<String, Object> errorAttributes) {
           //无code默认500
           if(!errorAttributes.containsKey("code")){
               return HttpStatus.INTERNAL_SERVER_ERROR.value();
           }
           int statusCode = (int) errorAttributes.get("code");
           return statusCode;
        }

        /**
         * 构建异常信息
         * @param request
         * @param ex
         * @return
        */
      private String buildMessage(ServerRequest request, Throwable ex) {
           StringBuilder message = new StringBuilder("Failed to handle request [");
           message.append(request.methodName());
           message.append(" ");
           message.append(request.uri());
           message.append("]");
           if (ex != null) {
               message.append(": ");
               message.append(ex.getMessage());
            }

           return message.toString();
     }

      /**
        * 构建返回的JSON数据格式
        * @param status        状态码
        * @param errorMessage  异常信息
        * @return
        */
      public static Map<String, Object> response(int status, String path, String errorMessage) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", status);
            map.put("message", errorMessage);
            map.put("data", null);
            map.put("path", path);

            if(!path.contains(".ico")) {
                logger.error(map.toString());
            }

            return map;
      }

}
