package org.wrh.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wrh.cloud.common.dto.ReturnResult;

/**
 * @author wuruohong
 * @date 2022-06-17 2:45
 */
@Slf4j
@RestController
@RequestMapping("self/")
public class RespController {

    @GetMapping("ask")
    public ReturnResult askInfo(@RequestParam("name") String name) {
        log.info("[self]>>>  {}  ask info", name );
        return ReturnResult.success("baidu hello " + name);
    }

    /**
     * 用来测试是否过滤器中的可以检查到这里面的异常
     * 这个里面的异常是zuul里面的异常处理器所无法解决的，
     * @param name
     * @return
     */
    @GetMapping("error")
    public String selfError(@RequestParam("name") String name) {
        log.info("[self]>>>  {}  error", name );
        int i = 1/0;
        return "hello" + name;
    }

    @GetMapping("common")
    public ReturnResult common(@RequestParam("name") String name) {
        log.info("[self]>>>  {}  common", name );
        return ReturnResult.success("common hello " + name);
    }
}
