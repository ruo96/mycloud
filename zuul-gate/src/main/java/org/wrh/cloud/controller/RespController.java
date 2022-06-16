package org.wrh.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuruohong
 * @date 2022-06-17 2:45
 */
@Slf4j
@RestController
@RequestMapping("self/")
public class RespController {

    @GetMapping("ask")
    public String askInfo(@RequestParam("name") String name) {
        log.info("  {}  ask info", name );
        return "hello" + name;
    }
}
