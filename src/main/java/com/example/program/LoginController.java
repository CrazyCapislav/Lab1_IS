/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.program;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Rahim
 */
@Controller
public class LoginController {


    @Autowired
    private UserRepo userRepo;

    @PostMapping("/auth/login")
    public String login(
        @RequestParam String username,
        @RequestParam String password,
        HttpSession session) {
        
        Optional<User> userOpt = userRepo.findByUsername(username);
        
        if (userOpt.isEmpty()){
            return "Invalid user";
        }
        User user = userOpt.get();

        if (!user.getPassword().equals(password)){
            return "Invalid password";
        }
        
        session.setAttribute("loggedInUser", username);
        return "redirect:/api/data";
    }

    @PostMapping("/auth/register")
    public String register(
        @RequestParam String username,
        @RequestParam String password,
        Model model) {

        if (userRepo.findByUsername(username).isPresent()){
            model.addAttribute("error", "UserExist");
            return "index";
        }
        
        User user = new User(username, password);

        userRepo.save(user);

        model.addAttribute("message", "Registered");
        return "redirect-wait";
    }

    @DeleteMapping("/users/{id}/delete")
    public String deleteUser(
        @PathVariable Long id,
        HttpSession session) {
        
        if (session.getAttribute("loggedInUser")==null){
            return "redirect:/";
        }
        
        if (userRepo.existsById(id)){
            userRepo.deleteById(id);
            session.setAttribute("loggedInUser",null);
            return "redirect:/?message=User deleted";
        } else{
            return "redirect:/?message=User not found";
        }
    }
    
}
