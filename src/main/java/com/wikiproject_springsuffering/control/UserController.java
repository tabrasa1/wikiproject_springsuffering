// package com.wikiproject_springsuffering.control;

// import com.wikiproject_springsuffering.model.User;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;

// @Controller
// public class UserController {
//     @GetMapping("/register")
//     public String showForm(Model model) {
//         model.addAttribute("user", new User());
//         return "register";
//     }
//     @PostMapping("/register")
//     public String submitForm(User user, Model model) {
//         model.addAttribute("message",
//         "Registered: " + user.getName() + " (" + user.getEmail() + ")");
//         return "result";
//     }
// }