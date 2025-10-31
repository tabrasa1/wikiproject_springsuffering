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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

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
    @Autowired
    private WarticleRepository warticleRepo;

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
        return "/wiki/categories";
    }

    //Delete category: handled by a button that POSTs from articles.html
    @PostMapping("/purge")
    @org.springframework.transaction.annotation.Transactional
    public String purgeCategory(@RequestParam("categoryId") Integer categoryId, HttpSession session, Model model, RedirectAttributes redirectAttrs) {
        // Admin session check
        if (session == null || session.getAttribute("loggedInAdmin") == null) {
            redirectAttrs.addFlashAttribute("resultMessage", "No permissions!");
            return "redirect:/wiki/categories";
        }

        // Uncategorized category check
        if (categoryId == 1) {
            redirectAttrs.addFlashAttribute("resultMessage", "Cannot delete that category!");
            return "redirect:/wiki/categories";
        }

        if (categoryId != 1) {
            categoryRepo.findById(categoryId).ifPresent(cat -> {
                    // Foreign key disassociation procedure
                    List<Warticle> articles = warticleRepo.findByCategory(cat);
                        if (articles != null && !articles.isEmpty()) {
                            // Find or create an "uncategorized" category
                            WikiCategory fallback = categoryRepo.findByName("Uncategorized").orElseGet(() -> {
                                WikiCategory fall = new WikiCategory();
                                fall.setName("Uncategorized");
                                return categoryRepo.save(fall);
                            });
                            for (Warticle a : articles) {
                                a.setCategory(fallback);
                            }
                            warticleRepo.saveAll(articles);
                        }
                        // Now safe to delete the category
                        categoryRepo.delete(cat);
                    } );
        }

        redirectAttrs.addFlashAttribute("resultMessage", "Category purged successfully.");
        return "redirect:/wiki/categories";
    }

    

}
