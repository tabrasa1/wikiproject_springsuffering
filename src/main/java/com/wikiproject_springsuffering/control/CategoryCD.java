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
@RequestMapping("/wiki/categories")
public class CategoryCD {

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new WikiCategory());
        model.addAttribute("categories", categoryRepo.findAll());
        return "wikicrud/createcat";
    }

    // Category creation
    @PostMapping("/new")
    public String createCategory(@ModelAttribute("category") WikiCategory categoryAdd,
            Model model) { //New category, therefore new instance
        
        // Store errors in a list
        List<String> errors = new ArrayList<>();

        // No null or empty category names
        Optional<WikiCategory> existing = categoryRepo.findByName(categoryAdd.getName());
        if (existing.isPresent()) {
            errors.add("Category name already exists!");
        }

        if (categoryAdd.getName() == null || categoryAdd.getName().trim().isEmpty()) {
            errors.add("Category name is required!");
        }

        //Return error upon presence of error messages
        if (!errors.isEmpty()) {
            model.addAttribute("message", errors);
            return "wikicrud/createcat";
        }

        categoryRepo.save(categoryAdd);
        return "redirect:/wiki/categories";
    }


}
