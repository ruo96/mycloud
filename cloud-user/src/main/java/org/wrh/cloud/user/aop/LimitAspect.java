package org.wrh.cloud.user.aop;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.wrh.cloud.common.annotation.Limit;
import org.wrh.cloud.common.dto.ReturnResult;
import org.wrh.cloud.common.enums.ResultEnum;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 使用AOP切面拦截限流注解
 * @date 2021/10/22
 * @author sirius
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {
    /**
     * 不同的接口，不同的流量控制
     * map的key为 Limiter.key
     */
    private final Map<String, RateLimiter> limiteMap = Maps.newConcurrentMap();

    @Around("@annotation(org.wrh.cloud.common.annotation.Limit)")
    public ReturnResult around(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        Method method = signature.getMethod();

        //拿limit的注解
        Limit limit = method.getAnnotation(Limit.class);
        if(limit != null){
            //key作用：不同的接口，不同的流量控制
            String key = limit.key();
            RateLimiter rateLimiter = null;
            //验证缓存是否有命中key
            if(!limiteMap.containsKey(key)){
                //创建令牌桶
                rateLimiter = RateLimiter.create(limit.permitsPerSecond());
                limiteMap.put(key, rateLimiter);
                log.info("新建了令牌桶={}，容量={}", key, limit.permitsPerSecond());
            }
            rateLimiter = limiteMap.get(key);

            //拿令牌
            boolean acquire = rateLimiter.tryAcquire(limit.timeout(), limit.timeunit());
            //拿不到令牌，直接返回限流异常提示
            // log.info("[limit-apo]>>> rate: {}", rateLimiter.);
            if(!acquire){
                log.info("令牌桶={},获取令牌失败", key);
                Class returnType = method.getReturnType();
                if(returnType!=null){
                    //CommonResult结果则返回限流提示
                    String returnTypeName = returnType.getName();
                    if(returnTypeName.contains("ReturnResult")){
                        return ReturnResult.fail(ResultEnum.FAIL);
                    }
                }
                return null;
            }
        }

        ReturnResult resp = (ReturnResult) joinPoint.proceed();
        return resp;
        // return joinPoint.proceed();
    }
}
