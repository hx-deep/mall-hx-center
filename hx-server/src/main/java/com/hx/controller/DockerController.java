package com.hx.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/hx")
@RestController
public class DockerController {


    @PostMapping("getStr")
    public String dockerMapping(@RequestBody Map<String, Object> param) {
        Object name = param.get("name");
        return name.toString();
    }

    @Value(value = "")
    public String hello;
}
