package com.wikiproject_springsuffering.control;

import com.wikiproject_springsuffering.model.Warticle;
import com.wikiproject_springsuffering.repository.WarticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/wiki")
public class WarticleCRUD {

    @Autowired
    private WarticleRepository warticleRepository;

    // READ: Display all articles
    @GetMapping("/")
    public String listArticles(Model model) {
        model.addAttribute("articles", warticleRepository.findAll());
        return "wikicrud/articles"; // Thymeleaf template
    }

    // READ: Display one article
    @GetMapping("/view/{id}")
    public String viewArticle(@PathVariable int id, Model model) {
    Optional<Warticle> article = warticleRepository.findById(id);
    if (article.isPresent()) {
        model.addAttribute("article", article.get());
        return "wikicrud/viewarticle";
    } else {
        return "wikicrud/notfound"; // Optional: handle missing article
    }
}


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

