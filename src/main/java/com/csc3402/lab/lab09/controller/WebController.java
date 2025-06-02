package com.csc3402.lab.lab09.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "crud";
    }

    @GetMapping("/crud")
    public String crudPage() {
        return "crud";
    }
}
