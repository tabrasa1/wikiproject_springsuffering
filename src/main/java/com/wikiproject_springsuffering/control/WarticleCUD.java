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
import jakarta.servlet.http.HttpSession; //admincheck

import java.util.List;
import java.util.ArrayList;
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

    // Evoke the update form, no update yet
    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Integer id, Model model, HttpSession session) {
        // Admin check
        if (session == null || session.getAttribute("loggedInAdmin") == null) {
            model.addAttribute("resultMessage", "No permissions to update articles.");
            return "redirect:/wiki/articles/" + id;
        }

        Warticle article = warticleRepo.findById(id).orElse(null);
        if (article == null) {
            model.addAttribute("resultMessage", "Article not found.");
            return "redirect:/wiki/articles";
        }

        model.addAttribute("article", article);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("tags", tagRepo.findAll());
        // Return the update article template
        return "wikicrud/updateart";
    }

    //Handle update POST
    @PostMapping("/{id}/update")
    public String updateArticle(@PathVariable Integer id,
                                @ModelAttribute("article") Warticle formUpdate,
                                @RequestParam(value = "category", required = false) Integer categoryId,
                                Model model, HttpSession session) {
        // Admin check
        if (session == null || session.getAttribute("loggedInAdmin") == null) {
            model.addAttribute("resultMessage", "No permission!");
            return "redirect:/wiki/articles/" + id;
        }

        // Null article check
        Warticle existing = warticleRepo.findById(id).orElse(null);
        if (existing == null) {
            model.addAttribute("resultMessage", "No article to edit!");
            return "/wiki/articles";
        }

        // Title and tag validations as per creation rules
        List<String> errors = new ArrayList<>();
        if (formUpdate.getTitle() == null || formUpdate.getTitle().trim().isEmpty()) {
            errors.add("Title is required!");
        }
        if (formUpdate.getTags() == null || formUpdate.getTags().isEmpty()) {
            errors.add("Minimum of 1 tag must be selected.");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("message", errors);
            model.addAttribute("article", existing);
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("tags", tagRepo.findAll());
            return "wikicrud/updateart";
        }

        // Apply updates
        existing.setTitle(formUpdate.getTitle());
        existing.setContents(formUpdate.getContents());
        existing.setHiddenFlag(formUpdate.isHiddenFlag());
        // Replace tags (binding should have populated Tag instances or proxies)
        existing.setTags(formUpdate.getTags());

        // Only update category if one was provided in the form/request.
        if (categoryId != null) {
            WikiCategory category = categoryRepo.findById(categoryId).orElse(null);
            existing.setCategory(category);
        }

        existing.setDateEdit(new Timestamp(System.currentTimeMillis()));

        warticleRepo.save(existing);
        return "redirect:/wiki/articles/" + id;
    }

    //Article creation
    @PostMapping("/new")
    public String createArticle(@ModelAttribute("article") Warticle article,
        @RequestParam("category") Integer categoryId,
        Model model, HttpSession session) {
        
        // Store errors in a list
        List<String> errors = new ArrayList<>();

        // Admin session check
        if (session == null || session.getAttribute("loggedInAdmin") == null) {
            model.addAttribute("resultMessage", "No permissions!");
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("tags", tagRepo.findAll());
            return "wikicrud/createart"; //we're not evaluating a thing more, ciao
        }

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

    // Delete article by id (POST)
    @PostMapping("/{id}/delete")
    public String deleteArticle(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttrs) {
        // Admin check
        if (session == null || session.getAttribute("loggedInAdmin") == null) {
            redirectAttrs.addFlashAttribute("resultMessage", "No permissions to delete!");
            return "redirect:/wiki/articles/" + id;
        }

        // Bye bye article
        java.util.Optional<Warticle> toDelete = warticleRepo.findById(id);
        if (toDelete.isPresent()) {
            Warticle bye = toDelete.get();
            warticleRepo.delete(bye);
        }

        redirectAttrs.addFlashAttribute("resultMessage", "Article deleted.");
        return "redirect:/wiki/articles";
    }

}
