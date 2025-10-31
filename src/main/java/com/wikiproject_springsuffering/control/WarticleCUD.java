package com.wikiproject_springsuffering.control;

import com.wikiproject_springsuffering.model.Warticle;
import com.wikiproject_springsuffering.model.WikiCategory;
import com.wikiproject_springsuffering.repository.WarticleRepository;
import com.wikiproject_springsuffering.repository.CategoryRepository;
import com.wikiproject_springsuffering.repository.TagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;

@Controller
@RequestMapping("/wiki/articles")
public class WarticleCUD {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private TagRepository tagRepo;


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Warticle());
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("tags", tagRepo.findAll());
        return "wikicrud/createart";
    }

    @Autowired
    private WarticleRepository warticleRepo;

    //Article creation
    @PostMapping("/new")
    public String createArticle(@ModelAttribute("article") Warticle article,
            @RequestParam("category") Integer categoryId,
            Model model) {
        
        // Store errors in a list
        List<String> errors = new ArrayList<>();

        // No null or empty titles. One tag minimum.
        if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
            errors.add("Title is required!");
        }

        if (article.getTags() == null || article.getTags().isEmpty()) {
        errors.add("Minimum of 1 tag must be selected.");
        }

        //Return error upon presence of error messages
        if (!errors.isEmpty()) {
            model.addAttribute("message", errors);
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("tags", tagRepo.findAll());
            return "wikicrud/createart";
        }


        article.setDateCreate(new Timestamp(System.currentTimeMillis()));
        article.setDateEdit(new Timestamp(System.currentTimeMillis()));

        WikiCategory category = categoryRepo.findById(categoryId).orElse(null);
        article.setCategory(category);

        warticleRepo.save(article);
        return "redirect:/wiki/articles";
    }


}
