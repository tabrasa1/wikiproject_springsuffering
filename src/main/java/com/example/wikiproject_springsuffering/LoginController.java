package com.example.wikiproject_springsuffering;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String hello(Model model) {
    model.addAttribute("helloMsg", "ARMOK'S WORST WIKI HOMEPAGE");
    return "login"; // resolves to hello.html in templates/
    }
}