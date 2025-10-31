package com.wikiproject_springsuffering.control;

import com.wikiproject_springsuffering.model.Warticle;
import com.wikiproject_springsuffering.model.WikiCategory;
import com.wikiproject_springsuffering.repository.WarticleRepository;
import com.wikiproject_springsuffering.repository.CategoryRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession; //User persistence

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Arrays;


@Controller
@RequestMapping("/wiki")
public class WarticleREAD {

    @Autowired
    private WarticleRepository warticleRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    //It's just the mainpage dawg
    @GetMapping("/")
    //get a load of this hot mess
    public String showMainWikiPage(Model model, HttpSession adminsess) {
    //what a horrifying way to do a boolean: true only if session exists, and has a non-null login
    boolean isAdmin = adminsess != null && adminsess.getAttribute("loggedInAdmin") != null;
    //no seriously these are wretched: true only if session exists, and showhidden is set to true
    boolean showHidden = adminsess != null && Boolean.TRUE.equals(adminsess.getAttribute("showHidden"));
    //self-explanatory repository queries
    List<Warticle> recentArticles = (isAdmin && showHidden)
        ? warticleRepository.findTop3ByOrderByDateCreateDesc()
        : warticleRepository.findTop3ByHiddenFlagFalseOrderByDateCreateDesc();

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
        //man i have to make a totally unique query for this one, annoying
        model.addAttribute("articlesCensored", warticleRepository.findByCategoryAndHiddenFlagFalse(category));
        model.addAttribute("categoryName", category.getName());
        //for the deletion logic
        model.addAttribute("categoryId", id);
        model.addAttribute("category", category);
        return "wikicrud/articles";
    }

    // READ: Display articles by searched tags
    @GetMapping("/articles/search")
    public String searchByTags(@RequestParam("tags") String tagInput, Model model) { //acquire tags from get request
        String[] tagNames = tagInput.split("[,\\s]+"); //parse between each space or comma
        List<Warticle> articles = warticleRepository.findByTags_Name //invoke the query from repository
        (Arrays.stream(tagNames) //clean tags and stuff em in a list
            .map(String::trim)
            .collect(Collectors.toList()));
        //it's a wee tad cursed to be plonking these
        //values in EVERY mapping but time is of the essence
        //and i'm short of it
        model.addAttribute("articles", articles); //values established for the viewer
        model.addAttribute("articlesCensored", warticleRepository.findByHiddenFlagFalse());
        model.addAttribute("activeTags", tagInput);
        return "wikicrud/articles";
    }

    //READ: Display articles by exact ID
    @GetMapping("/articles/{id}")
    public String articleByID(@PathVariable Integer id, Model model) { //grab id from url
        Optional<Warticle> accessArticle = warticleRepository.findById(id); //evoke query prepared in repository
        Warticle articleFetch = accessArticle.orElse(null); //give me an article or give me null
        model.addAttribute("article", articleFetch); //make articleFetch available to viewer

    return "wikicrud/viewarticle.html";
    }
    // READ: Display all articles
    @GetMapping("/articles")
    public String listArticles(Model model) {
        model.addAttribute("articles", warticleRepository.findAll());
        model.addAttribute("articlesCensored", warticleRepository.findByHiddenFlagFalse());
        return "wikicrud/articles"; // .html template
    }

    // Admin toggle for showing hidden articles in the UI (does not alter DB flags)
    @GetMapping("/admin/toggleHidden")
    public String toggleShowHidden(@RequestParam(value = "state", required = false) Boolean state,
                                   HttpSession session,
                                   @RequestHeader(value = "Referer", required = false) String referer) {
        if (session == null || session.getAttribute("loggedInAdmin") == null) {
            return "redirect:/wiki/";
        }
        Boolean current = (Boolean) session.getAttribute("showHidden");
        boolean next = (state != null) ? state : !(current != null && current);
        session.setAttribute("showHidden", next);
        return "redirect:" + (referer != null ? referer : "/wiki/articles");
    }
}

