package org.dangnd.kafka.controller;

import org.dangnd.kafka.service.TestAspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestAspService service;

    @GetMapping
    public String test() {
        service.execute();
        return "OK";
    }
}
