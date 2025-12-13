package ru.ythree.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloRestController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "<h1>Hello, world!</h1>";
    }
}
