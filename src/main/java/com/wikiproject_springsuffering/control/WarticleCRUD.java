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
public class WarticleCRUD {

    @Autowired
    private WarticleRepository warticleRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    //It's just the mainpage dawg
    @GetMapping("/")
    public String showMainWikiPage(Model model) {
        List<Warticle> recentArticles = warticleRepository.findTop3ByOrderByDateCreateDesc();
        model.addAttribute("recentArticles", recentArticles);
        return "wikicrud/wikimain";
    }

    // READ: Display all categories
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "wikicrud/categories"; // .html template
    }

    // READ: Display articles by category
    @GetMapping("/categories/{id}")
    public String articlesByCategory(@PathVariable Integer id, Model model) {
        WikiCategory category = categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));

        List<Warticle> articles = warticleRepository.findByCategory(category);
        model.addAttribute("articles", articles);
        model.addAttribute("categoryName", category.getName());
        return "wikicrud/articles";
    }

    // READ: Display articles by searched tags
    @GetMapping("/articles/search")
    public String searchByTags(@RequestParam("tags") String tagInput, Model model) {
        String[] tagNames = tagInput.split(","); //break it up between each comma
        List<Warticle> articles = warticleRepository.findByTags_Name(Arrays.stream(tagNames)
            .map(String::trim)
            .collect(Collectors.toList()));
        model.addAttribute("articles", articles);
        model.addAttribute("activeTags", tagInput);
        return "wikicrud/articles";
    }

    //READ: Display articles by exact ID
    @GetMapping("/articles/{id}")
    public String articleByID(@PathVariable Integer id, Model model) {
        Optional<Warticle> accessArticle = warticleRepository.findById(id);
        Warticle articleFetch = accessArticle.orElse(null);
        model.addAttribute("article", articleFetch);    

    return "wikicrud/viewarticle.html";
    }
    // READ: Display all articles
    @GetMapping("/articles")
    public String listArticles(Model model) {
        model.addAttribute("articles", warticleRepository.findAll());
        return "wikicrud/articles"; // .html template
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

