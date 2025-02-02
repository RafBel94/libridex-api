package com.rafbel94.libridex_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class IndexController {

    private final String INDEX_VIEW = "index";

    @GetMapping("")
    public String getMethodName() {
        return INDEX_VIEW;
    }
    
}
