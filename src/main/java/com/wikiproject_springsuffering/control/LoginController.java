package com.wikiproject_springsuffering.control;
import com.wikiproject_springsuffering.model.Admin;
import com.wikiproject_springsuffering.repository.AdminRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.mindrot.jbcrypt.BCrypt;


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
        if (adminForm.getUsername() == null
        || adminForm.getUsername().trim().isEmpty()
        || adminForm.getPassword() == null
        || adminForm.getPassword().trim().isEmpty()) {
        model.addAttribute("resultMessage", "Blank form submissions should not be possible!");
        return "login"; //Redundant, but avoids nested else statement
        }
        //Get hashes ready
        String DBHash = adminRepoConn.findPasswordHashByUsername(adminForm.getUsername());
        Boolean submitHash = BCrypt.checkpw(adminForm.getPassword(), DBHash);

        //Test username and password hashes
        if (adminRepoConn.findByUsername(adminForm.getUsername()).isPresent()
        && (submitHash==true) ){
            model.addAttribute("resultMessage", "Login successful!");
        }
        else {
            model.addAttribute("resultMessage", "No credential match!");
        }
        //Reset the form or else it bricks
        model.addAttribute("adminForm", new Admin());
        return "login";
   }
}