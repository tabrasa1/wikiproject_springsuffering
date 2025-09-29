package com.wikiproject_springsuffering.control;
import com.wikiproject_springsuffering.model.Admin;
import com.wikiproject_springsuffering.repository.AdminRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
//Establish the AdminRepository class for use, establish binding
public class LoginController {
    private final AdminRepository adminRepoConn;

    public LoginController(AdminRepository adminRepoAccess) {
        this.adminRepoConn = adminRepoAccess;
    }

    //Establish a form with Admin model for data
    @GetMapping("/login")
    public String showForm(Model model) {
        model.addAttribute("adminForm", new Admin());
        return "login";
    }
    
    @PostMapping("/login")
    public String submitForm(Admin adminForm, Model model) {
        if (adminForm.getUsername() == null || adminForm.getUsername().trim().isEmpty() ||
        adminForm.getPassword() == null || adminForm.getPassword().trim().isEmpty()) {
        model.addAttribute("resultMessage", "Blank forms should not be possible!");
        }
        else if (adminRepoConn.findByUsername(adminForm.getUsername()).isPresent()) {
            model.addAttribute("resultMessage", "Username confirmed!");

            //todo: die a hero
        }
        else{
            model.addAttribute("resultMessage", "No match!");
        }
        model.addAttribute("adminForm", new Admin()); // Reset the form
        return "login";
   }
}