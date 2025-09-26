package com.example.wikiproject_springsuffering;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
    model.addAttribute("helloMsg", "BIT235 Spring Boot + Thymeleaf tutorial");
    return "hello"; // resolves to hello.html in templates/
    }
}