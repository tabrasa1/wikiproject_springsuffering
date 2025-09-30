package com.wikiproject_springsuffering.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Random; //patently frivulous

@Controller
public class HelloController {
    @GetMapping("/index")
    //Helloworld replacer
    public String hello(Model model) {
    model.addAttribute("helloMsg", "ARMOK'S WORST WIKI HOMEPAGE");

    //Random BG for my own amusement
    String[] colors = { "#FF99AA", "#7397a3ff", "#74bf74ff", "#e3da83ff", "#c282c2ff"};
    String randomColor = colors[new Random().nextInt(colors.length)];
    model.addAttribute("randomColor", randomColor);
    return "index"; // hooks to to index.html in templates/
    }
}