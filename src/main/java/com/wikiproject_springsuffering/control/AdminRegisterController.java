package com.wikiproject_springsuffering.control;
import com.wikiproject_springsuffering.model.Admin;
import com.wikiproject_springsuffering.repository.AdminRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//Establish the AdminRepository class for use, establish binding
public class AdminRegisterController {
    private final AdminRepository adminRepoConn;

    public AdminRegisterController(AdminRepository adminRepoAccess) {
        this.adminRepoConn = adminRepoAccess;
    }

    //Establish a form with Admin model for data
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("adminForm", new Admin());
        return "register";
    }
    
    @PostMapping("/register")
    public String submitForm(Admin adminForm, Model model) {
        if (adminForm.getUsername() == null
        || adminForm.getUsername().trim().isEmpty()
        || adminForm.getPassword() == null
        || adminForm.getPassword().trim().isEmpty()) {
        model.addAttribute("resultMessage", "Blank form submissions should not be possible!");
        }
        else if (adminRepoConn.findByUsername(adminForm.getUsername()).isPresent()) {
            model.addAttribute("resultMessage", "Username is already taken!");
        } else {
            adminForm.setPassword_hash(String.valueOf(adminForm.getPassword().hashCode()));
            //adminRepoConn.save(adminForm); Disabled for testing purposes
            model.addAttribute("resultMessage", "User "+adminForm.getUsername()+" registered!");
        }
        
        //Reset the form or else it bricks
        model.addAttribute("adminForm", new Admin());
        return "register";
    }
}