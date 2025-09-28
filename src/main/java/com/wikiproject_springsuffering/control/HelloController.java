package com.wikiproject_springsuffering.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/index")
    public String hello(Model model) {
    model.addAttribute("helloMsg", "ARMOK'S WORST WIKI HOMEPAGE");
    return "index"; // resolves to hello.html in templates/
    }
}