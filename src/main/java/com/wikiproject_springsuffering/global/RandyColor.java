package com.wikiproject_springsuffering.global;

import org.springframework.web.bind.annotation.ControllerAdvice;//needed for controlleradvice
import org.springframework.web.bind.annotation.ModelAttribute;//needed for modelattribute
import org.springframework.ui.Model;
import java.util.Random; //patently frivulous

@ControllerAdvice
public class RandyColor {

    @ModelAttribute
    public void addGlobalAttributes(Model model) {

        String[] colors = { "#FF99AA", "#7397a3ff", "#74bf74ff", "#e3da83ff", "#c282c2ff" };
        String randomColor = colors[new Random().nextInt(colors.length)];
        model.addAttribute("randomColor", randomColor);
    }
}
