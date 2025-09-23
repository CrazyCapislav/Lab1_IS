package com.example.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;


/**
 *
 * @author Rahim
 */
@Controller
public class DataController {
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/api/data")
    public String getData(HttpSession session, Model model) {
        
        String currentUser = (String) session.getAttribute("loggedInUser");
        if (currentUser != null){
            model.addAttribute("users", userRepo.findAll());
            var opt = userRepo.findByUsername(currentUser);
            if (opt.isPresent()){
                model.addAttribute("currentUser",currentUser);
                model.addAttribute("currentUserId", opt.get().getId());
            }
        }
        return "data";
    }
}
