package com.wikiproject_springsuffering.control;
import com.wikiproject_springsuffering.model.Customer;
import com.wikiproject_springsuffering.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }
    
    @PostMapping("/register")
    public String submitForm(Customer customer, Model model) {
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            model.addAttribute("message", "Username is already taken!");
        } else {
            customer.setPassword_hash(Integer.toHexString(customer.getPassword().hashCode()));
            customerRepository.save(customer);
            model.addAttribute("message", "User registered!" + customer.getUsername());
        }
        return "result";
    }
}