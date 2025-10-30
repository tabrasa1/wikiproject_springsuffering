package com.wikiproject_springsuffering.control;

import com.wikiproject_springsuffering.model.Warticle;
import com.wikiproject_springsuffering.model.WikiCategory;
import com.wikiproject_springsuffering.repository.WarticleRepository;
import com.wikiproject_springsuffering.repository.CategoryRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Arrays;


@Controller
@RequestMapping("/wiki")
public class WarticleCUD {

    @Autowired
    private WarticleRepository warticleRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // CREATE: Show form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("warticle", new Warticle());
        return "wikicrud/create"; // Thymeleaf template
    }

    // CREATE: Handle form submission
    @PostMapping("/save")
    public String saveArticle(@ModelAttribute Warticle warticle) {
        warticleRepository.save(warticle);
        return "redirect:/wikicrud/articles";
    }

    // UPDATE: Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Warticle warticle = warticleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid article ID: " + id));
        model.addAttribute("warticle", warticle);
        return "wikicrud/update"; // Thymeleaf template
    }

    // UPDATE: Handle edit submission
    @PostMapping("/update/{id}")
    public String updateArticle(@PathVariable Integer id, @ModelAttribute Warticle warticle) {
        warticle.setId(id);
        warticleRepository.save(warticle);
        return "redirect:/wikicrud/articles";
    }

    // DELETE
    @GetMapping("/delete/")
    public String deleteArticle(@PathVariable Integer id) {
        warticleRepository.deleteById(id);
        return "redirect:/wikicrud/articles";
    }
}

